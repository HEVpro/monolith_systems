FROM python:3.8-slim-buster

WORKDIR /app

COPY ./compose/run.sh run.sh
RUN chmod +x run.sh

COPY requirements.txt requirements.txt
COPY db db
COPY settings.py settings.py
COPY link_consumer.py link_consumer.py
COPY user_consumer.py user_consumer.py
COPY link_metric_consumer.py link_metric_consumer.py
COPY test.py test.py

RUN pip3 install -r requirements.txt

CMD ./run.sh