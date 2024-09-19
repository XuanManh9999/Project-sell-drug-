import { Routes, Route } from "react-router-dom";
import PATH from "./utils/utils-url-route";
import { Login, ForgotPassword, Register, NotFound } from "./container/public";
import {
  HomeAdmin,
  ManageUser,
  ManageMedicine,
  ManageCategory,
} from "./container/admin";
function App() {
  const publicRoutes = [
    { paths: [PATH.HOME, PATH.LOGIN], element: <Login /> },
    { path: PATH.FORGOTPASSWORD, element: <ForgotPassword /> },
    { path: PATH.REGISTER, element: <Register /> },
  ];
  return (
    <>
      {/* Private */}
      <Routes>
        <Route path={PATH.MANAGE} element={<HomeAdmin />}>
          <Route path={PATH.MANAGE_USER} element={<ManageUser />} />
          <Route path={PATH.MANAGE_CATEGORY} element={<ManageCategory />} />
          <Route path={PATH.MANAGE_MEDICINE} element={<ManageMedicine />} />
        </Route>
      </Routes>
      {/* Public */}
      <Routes>
        {publicRoutes.map(({ paths, path, element }) => {
          if (paths) {
            return paths.map((p) => (
              <Route key={p} path={p} element={element} />
            ));
          }
          return <Route key={path} path={path} element={element} />;
        })}
      </Routes>
    </>
  );
}

export default App;
