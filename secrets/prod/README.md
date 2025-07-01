# Oneblog Deployment

## Local Development
`bash make local`


## Production Deploy    
1. Set up secrets in `secrets/prod/`:
    - `db_password.txt`
    - `google_client-secret.txt`
    - `mail_password.txt`
    - `jwt_secret.txt`
2. Deploy: `bash make deploy`