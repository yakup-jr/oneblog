if [ "$environment" = "local" ]; then
  docker config create promtail-config /config/local/promtail.yml
  docker config loki-conifg /config/local/loki.yml
  docker conifg prometheus-config /config/local/prometheus.yml
  docker config grafana-datasources /config/local/datasources/yml
elif [ "$environment" = "prod" ]; then
  docker config create promtail-config config/prod/promtail.yaml
  docker config create loki-config config/prod/loki.yaml
  docker config create prometheus-config config/prod/prometheus.yml
  docker config create grafana-datasources config/prod/datasources.yaml
fi

