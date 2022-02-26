FROM python:3.8-slim-buster

WORKDIR /app

COPY db db
COPY src src

WORKDIR src

RUN pip3 install -r requirements.txt

ENV PYTHONPATH /app

CMD ["python3", "consumer.py"]