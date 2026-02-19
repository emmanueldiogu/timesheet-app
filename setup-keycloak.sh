#!/bin/bash
# Assumes Keycloak running, realm exists

# Create test user + role
curl -X POST http://localhost:9080/admin/realms/timesheet/users \
    -H "Authorization: Bearer $(curl -s -X POST http://localhost:9080/realms/master/protocol/openid-connect/token -d 'username=admin&password=admin&grant_type=password&client_id=admin-cli' | jq -r .access_token)" \
    -H "Content-Type: application/json" \
    -d '{
    "username": "test",
    "email": "test@qodesquare.com",
    "enabled": true,
    "credentials": [{"type": "password", "value": "test123", "temporary": false}]
    }'

# Assign EMPLOYEE role
echo "User created. Assign EMPLOYEE role in Keycloak admin."
