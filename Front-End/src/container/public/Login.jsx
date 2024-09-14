import { useEffect, useState } from "react";
import { testFetchData } from "../../services/api-user";
function Login() {
  const [data, setData] = useState([]);
  useEffect(() => {
    const fetch = async () => {
      const response = await testFetchData();
      setData(response);
    };
    fetch();
  }, []);
  console.log("Xuan manh check data", data);

  return <h1>Login</h1>;
}

export default Login;
