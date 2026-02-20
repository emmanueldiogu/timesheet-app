import { useEffect, useState } from "react";
import keycloak from "./keycloak";
import BackendAPI from "./components/BackendAPI";

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const [username, setUsername] = useState("");

  useEffect(() => {
    keycloak
      .init({
        onLoad: "check-sso",
        pkceMethod: "S256",
      })
      .then((auth) => {
        console.log("Keycloak initialized:", auth);
        setAuthenticated(auth);
        if (auth && keycloak.tokenParsed) {
          console.log("Token parsed:", keycloak.tokenParsed);
          setUsername(keycloak.tokenParsed.preferred_username);
        }
      })
      .catch((error) => {
        console.error("Keycloak initialization failed:", error);
      })
      .finally(() => {
        setLoading(false); // Ensure loading is set to false even if there's an error
      });
  }, []);

  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading...
      </div>
    );

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
            onClick={() => keycloak.logout()}
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
