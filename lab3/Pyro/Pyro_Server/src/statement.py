class Statement:
    def __init__(self, account_id, type, message, timestamp) -> None:
        self.account_id = account_id
        self.type = type
        self.message = message
        self.timestamp = timestamp