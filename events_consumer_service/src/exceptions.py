class ConsumerException(Exception):
    def __init__(self, message="Event cannot be saved on DB"):
        self.message = message
        super().__init__(self.message)
