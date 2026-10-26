# 05 — Buno Ops Sentinel
API para ingestão de eventos operacionais, listagem e análise assistida pelo Buno Aegis Runtime. Expõe métricas via Actuator/Prometheus.
`docker compose up --build`; API `http://localhost:8105`; Prometheus `http://localhost:9095`; Grafana `http://localhost:3005` (admin/admin, somente local).
- `POST /api/ops/events` body `{"source":"payments-api","severity":"ERROR","message":"Timeout ao consultar provedor"}`
- `GET /api/ops/events`
- `POST /api/ops/analyze` body `{"question":"Quais padrões aparecem e qual próximo passo seguro?"}`
Eventos armazenados em memória. Para produção, conecte Loki/OTel, alert rules, armazenamento durável, retenção, controle de acesso e limites. O agente só recomenda; não executa remediação.
