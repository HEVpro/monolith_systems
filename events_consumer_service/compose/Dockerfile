FROM python:3.8-slim-buster

WORKDIR /app

COPY requirements.txt requirements.txt
COPY consumer.py consumer.py
COPY settings.py settings.py

RUN pip3 install -r requirements.txt

CMD ["python3", "consumer.py"]