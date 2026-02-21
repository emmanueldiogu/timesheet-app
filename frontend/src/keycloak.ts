import Keycloak from 'keycloak-js';

interface KeycloakConfig {
  url: string;
  realm: string;
  clientId: string;
}

// Store an instance in a variable that persists across HMR
let keycloakInstance: Keycloak | undefined;

// Check if we need a fresh instance (page reload vs HMR)
const needsFreshInstance = () => {
  // If no instance exists, we need one
  if (!keycloakInstance) return true;

  // If instance exists but not initialized, reuse it
  // Check if the instance is initialized by looking at public properties
  if (!keycloakInstance.authenticated && !keycloakInstance.token) return false;

  // If we have a code in URL, this is a redirect callback - need fresh instance
  return window.location.search.includes('code=') || window.location.hash.includes('code=');


};

if (needsFreshInstance()) {
  keycloakInstance = new Keycloak({
    url: import.meta.env.VITE_KEYCLOAK_URL || 'http://localhost:9080',
    realm: import.meta.env.VITE_KEYCLOAK_REALM || 'timesheet',
    clientId: 'react-frontend',
  } as KeycloakConfig);
}

if (!keycloakInstance) {
  throw new Error('Keycloak instance initialization failed');
}

export default keycloakInstance as Keycloak;
