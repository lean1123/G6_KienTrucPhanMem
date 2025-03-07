import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import AdminLayout from "./components/AdminLayout";
import LoginForm from "./components/LoginForm";
import { introspectToken } from "./hooks/auth/authSlice";
import { AppDispatch } from "./hooks/redux/store";
import AddCollection from "./pages/AddCollection";
import AddProductItem from "./pages/AddProductItem";
import CreateCategory from "./pages/CreateCategory";
import CreateColor from "./pages/CreateColor";
import CreateProduct from "./pages/CreateProduct";
import Dashboard from "./pages/Dashboard";
import EditCategory from "./pages/EditCategory";
import EditCollection from "./pages/EditCollection";
import EditColor from "./pages/EditColor";
import EditProduct from "./pages/EditProduct";
import EditProductItem from "./pages/EditProductItem";
import EditUser from "./pages/EditUser";
import ListCategory from "./pages/ListCategory";
import ListCollection from "./pages/ListCollection";
import ListColor from "./pages/ListColor";
import ListOrder from "./pages/ListOrder";
import ListProduct from "./pages/ListProduct";
import ListProductItem from "./pages/ListProductItem";
import ListUser from "./pages/ListUser";
import OrderDetail from "./pages/OrderDetail";
import UserDetail from "./pages/UserDetail";

function App() {
  const { isAuthenticated } = useSelector((state: any) => state.auth);
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
    dispatch(introspectToken());
  }, []);

  if (!isAuthenticated) {
    return (
      <Routes>
        <Route
          path="/"
          element={
            isAuthenticated ? <Navigate to="/" /> : <Navigate to="/login" />
          }
        />
        <Route path="/login" element={<LoginForm />} />
      </Routes>
    );
  }

  return (
    // <div className="App">
    <AdminLayout>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/products/list" element={<ListProduct />} />
        <Route path="/products/add" element={<CreateProduct />} />
        <Route path="/products/edit/:id" element={<EditProduct />} />
        <Route path="/products/:id/add-item" element={<AddProductItem />} />
        <Route path="/products/:id/list-item" element={<ListProductItem />} />
        <Route
          path="/products/:id/list-item/:itemId/edit"
          element={<EditProductItem />}
        />
        <Route path="/colors/list" element={<ListColor />} />
        <Route path="/colors/add" element={<CreateColor />} />
        <Route path="/colors/:id/edit" element={<EditColor />} />
        <Route path="/colors/add-collection/:id" element={<AddCollection />} />
        <Route
          path="/brands/view-collection/:id"
          element={<ListCollection />}
        />
        <Route
          path="/brands/view-collection/:id/:collectionId/edit"
          element={<EditCollection />}
        />
        <Route path="/categories/list" element={<ListCategory />} />
        <Route path="/categories/add" element={<CreateCategory />} />
        <Route path="/categories/:id/edit" element={<EditCategory />} />
        <Route path="/users/list" element={<ListUser />} />
        <Route path="/users/:id" element={<UserDetail />} />
        <Route path="/users/:id/edit" element={<EditUser />} />
        <Route path="/orders" element={<ListOrder />} />
        <Route path="/orders/:id" element={<OrderDetail isEdit={false} />} />
        <Route
          path="/orders/:id/edit"
          element={<OrderDetail isEdit={true} />}
        />
      </Routes>
    </AdminLayout>
    // </div>
  );
}

export default App;
