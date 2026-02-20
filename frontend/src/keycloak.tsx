import Keycloak from 'keycloak-js';

interface KeycloakConfig {
  url: string;
  realm: string;
  clientId: string;
}

const keycloak = new Keycloak({
  url: (import.meta as any).env.VITE_KEYCLOAK_URL || 'http://localhost:9080',
  realm: (import.meta as any).env.VITE_KEYCLOAK_REALM || 'timesheet',
  clientId: 'react-frontend',
} as KeycloakConfig);

export default keycloak;
