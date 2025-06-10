.PHONY: local prod clean

local:
    ./deploy-local.sh

prod:
    ./deploy-prod.sh

clean:
    docker stack rm oneblog-dev || true
    docker stack rm oneblog-prod || true
    sleep 10
    docker system prune -f