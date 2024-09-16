import { Routes, Route } from "react-router-dom";
import PATH from "./utils/utils-url-route";
import { Login, ForgotPassword, Register, NotFound } from "./container/public";
import { HomeAdmin, ManageUser } from "./container/admin";
function App() {
  const publicRoutes = [
    { paths: [PATH.HOME, PATH.LOGIN], element: <Login /> },
    { path: PATH.FORGOTPASSWORD, element: <ForgotPassword /> },
    { path: PATH.REGISTER, element: <Register /> },
  ];
  //     { path: PATH.NOTFOUND, element: <NotFound /> },
  const privateRoutes = [{ path: PATH.MANAGE, element: <HomeAdmin /> }];
  return (
    <>
      {/* Private */}
      <Routes>
        <Route path={PATH.MANAGE} element={<HomeAdmin />}>
          <Route path={PATH.MANAGE_USER} element={<ManageUser />} />
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
