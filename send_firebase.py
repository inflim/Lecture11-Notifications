#!/usr/local/bin/python3
import sys
from pyfcm import FCMNotification

API_KEY = "AAAA80r_s5Y:APA91bFfnBAu5l5tHpO3L23lcKLTC-psTFGj2ulj-uP-s99q5BpEAsWy8W_dRErY10Tp3BgW_wX7C2Hx-AWDmL8cqum9Z4hK-PebFTAC8OeLbKWQ-3junHpPnTKBvvH66l8Ktt8hBbjm"

if len(sys.argv) != 4:
    print("usage:" + sys.argv[0] + " <token> <title> <text>")
    exit(1)

push_service = FCMNotification(API_KEY)

data_message = {
    "Nick" : "Mario",
    "body" : "great match!",
    "Room" : "PortugalVSDenmark"
}
registration_id = sys.argv[1]
message_title = sys.argv[2]
message_body = sys.argv[3]

# send notification message
result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
print("send notification message result: ")
print(result)

# send notification & data message
# result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
# print("send notification & data message result: ")
# print(result)

# send data only message
# result = push_service.single_device_data_message(registration_id=registration_id, data_message=data_message)
# print("send data message result: ")
# print(result)
