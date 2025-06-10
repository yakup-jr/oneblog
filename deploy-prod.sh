for secret in db_password google_client_secret mail_username mail_password jwt_secret; do
  if [ ! "$(docker secret ls -q -f name=$secret)" ]; then
    docker secret create $secret /secrets/prod/$secret.txt
  fi
done

docker stack deploy -c docker-compose.yml oneblog-prod