import { Routes, Route } from "react-router-dom";
import PATH from "./utils/utils-url-route";
import { Login, ForgotPassword, Register, NotFound } from "./container/public";
import { ToastContainer } from "react-toastify";
import {
  HomeAdmin,
  ManageUser,
  ManageMedicine,
  ManageCategory,
  ManageBill,
  RevenueStatistics,
  DetailBill,
} from "./container/admin";
function App() {
  const publicRoutes = [
    { paths: [PATH.HOME, PATH.LOGIN], element: <Login /> },
    { path: PATH.FORGOTPASSWORD, element: <ForgotPassword /> },
    { path: PATH.REGISTER, element: <Register /> },
  ];
  return (
    <>
      <main>
        <Routes>
          <Route path={PATH.MANAGE} element={<HomeAdmin />}>
            <Route path={PATH.MANAGE_USER} element={<ManageUser />} />
            <Route path={PATH.MANAGE_CATEGORY} element={<ManageCategory />} />
            <Route path={PATH.MANAGE_MEDICINE} element={<ManageMedicine />} />
            <Route path={PATH.MANAGE_BILL} element={<ManageBill />} />
            <Route path={PATH.DETAIL_BILL} element={<DetailBill />} />
            <Route path={PATH.MANAGE_MONEY} element={<RevenueStatistics />} />
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
        <ToastContainer
          position="top-right"
          autoClose={1500}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
      </main>
    </>
  );
}

export default App;
