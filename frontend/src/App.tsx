import { useEffect, useState } from "react";
import keycloak from "./keycloak";
import BackendAPI from "./components/BackendAPI";

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const [username, setUsername] = useState("");
  const [initError, setInitError] = useState<string | null>(null);
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    let ignore = false;
    let isInitializing = false;

    const initKeycloak = async () => {
      // Prevent double initialization only within the same effect cycle
      if (isInitializing) {
         return;
      }

      isInitializing = true;

      try {
        const auth = await keycloak.init({
          onLoad: "check-sso",
          pkceMethod: "S256",
        });

        if (ignore) return;

        setInitialized(true);
        setAuthenticated(auth);
        if (auth && keycloak.tokenParsed) {
          setUsername(keycloak.tokenParsed.preferred_username || "");
        }
      } catch (error) {
        if (ignore) return;

        // If it's the double initialization error from StrictMode, treat as already initialized
        if (error instanceof Error && error.message.includes("initialized once")) {
          setInitialized(true);
          setAuthenticated(keycloak.authenticated || false);
          if (keycloak.authenticated && keycloak.tokenParsed) {
            setUsername(keycloak.tokenParsed.preferred_username || "");
          }
        } else {
          setInitError(error instanceof Error ? error.message : "Unknown error");
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
        isInitializing = false;
      }
    };

    initKeycloak();

    return () => {
      ignore = true;
    };
  }, []);

  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading...
      </div>
    );

  if (initError) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-red-50 p-8">
        <div className="text-2xl font-bold text-red-600 mb-4">
          Keycloak Initialization Failed
        </div>
        <div className="text-red-800 mb-4 max-w-2xl text-center">
          {initError}
        </div>
        <button
          onClick={() => window.location.reload()}
          className="bg-red-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-red-700"
        >
          Retry
        </button>
      </div>
    );
  }

  if (!authenticated) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-linear-to-br from-blue-500 to-purple-600 p-8">
        <div className="text-4xl font-bold text-white mb-8">
          React + Keycloak PKCE 🚀
        </div>
        <button
          onClick={() => keycloak.login()}
          className="bg-white text-blue-600 px-8 py-4 rounded-lg text-xl font-semibold hover:shadow-xl"
        >
          Login
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-4xl mx-auto px-6 py-6">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Dashboard</h1>
          <p className="text-xl text-gray-600">
            Welcome,{" "}
            <span className="font-semibold text-blue-600">{username}</span>!
          </p>
          <button
            onClick={() => keycloak?.logout()}
            className="mt-4 bg-red-500 hover:bg-red-600 text-white px-6 py-2 rounded-md font-medium"
          >
            Logout
          </button>
        </div>
      </header>
      <main className="max-w-4xl mx-auto px-6 py-12">
        <BackendAPI />
      </main>
    </div>
  );
}

export default App;
