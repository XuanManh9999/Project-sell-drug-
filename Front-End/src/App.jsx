import { Routes, Route } from "react-router-dom";
import PATH from "./utils/utils-url-route";
import { Login, ForgotPassword, Register, NOTFOUND } from "./container/public";
function App() {
  return (
    <>
      {/* PUBLIC */}
      <Routes>
        <Route path={PATH.HOME} element={<Login />} />
        <Route path={PATH.LOGIN} element={<Login />} />
        <Route path={PATH.FORGOTPASSWORD} element={<ForgotPassword />} />
        <Route path={PATH.REGISTER} element={<Register />} />
        <Route path={PATH.NOTFOUND} element={<NOTFOUND />} />
      </Routes>
    </>
  );
}

export default App;
