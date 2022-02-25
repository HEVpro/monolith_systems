FROM python:3.8-slim-buster

WORKDIR /app

COPY requirements.txt requirements.txt
COPY db db
COPY settings.py settings.py
COPY consumer.py consumer.py

RUN pip3 install -r requirements.txt

CMD ["python3", "consumer.py"]