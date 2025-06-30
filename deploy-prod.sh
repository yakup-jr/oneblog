#!/bin/bash
set -e
set -x

for secret in db_username db_password oauth_google_client_id oauth_google_client_secret \
mail_username mail_password jwt_secret_key; do
  if [ ! -s "./secret/prod/$secret.txt" ]; then
    echo "Missing or empty: ./secret/prod/$secret.txt"
    exit 1
  fi
  if [ ! "$(docker secret ls -q -f name=$secret)" ]; then
    docker secret create $secret /secret/prod/$secret.txt
  fi
done

for config in grafana loki prometheus promtail; do
  if [ ! -s "./config/prod/$config.yml" ]; then
    echo "Missing or empty: ./config/prod/$config.yml"
    exit 1
  fi
  if [ ! "$(docker config ls -q -f name=$config)" ]; then
    docker config create $config /config/prod/$config.yml
  fi
done


if [ ! "$(docker config ls -q -f name=haproxy)" ]; then
  if [ ! -s "./config/prod/haproxy.cfg" ]; then
      echo "Missing or empty: ./config/prod/haproxy.cfg"
      exit 1
  fi
  docker config create haproxy /config/prod/haproxy.cfg
fi

docker stack deploy -c ./docker/prod/docker-compose.yml oneblog