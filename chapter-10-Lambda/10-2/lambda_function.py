import json
import logging
import base64
import gzip
import boto3
import os

logger = logging.getLogger()
logger.setLevel(logging.INFO)
sns_client = boto3.client('sns')

def lambda_handler(event, context):
    
    # CloudWatch Logsからのデータ取得
    cw_data = event['awslogs']['data']
    # Base64デコード
    compressed_payload = base64.b64decode(cw_data)
    # gzip解凍
    uncompressed_payload = gzip.decompress(compressed_payload)
   
    payload = json.loads(uncompressed_payload)
    logger.info(payload)
   
    # CloudWatch Logsのメタデータ取得してメッセージ本文に追加
    message = 'messageType=' + payload['messageType'] + '\n' \
        'owner=' + payload['owner'] + '\n' \
        'logGroup=' + payload['logGroup'] + '\n' \
        'logStream=' + payload['logStream'] + '\n' \
        '\n' \
        'ログメッセージ\n'

    # ログ内容を取得してメッセージ本文に追加
    log_events = payload['logEvents']
    for log_event in log_events:
        logger.info(log_event)
        message += str(log_event) + '\n'
    
    # 環境変数からSNSのARNを取得
    sns_topic_arn = os.environ['SNS_TOPIC_ARN']
    
    # SNSにpublish
    response = sns_client.publish(
        TopicArn = sns_topic_arn,
        Message = message,
        Subject = 'エラー通知',
        MessageStructure = 'string'
    )
    
    return response