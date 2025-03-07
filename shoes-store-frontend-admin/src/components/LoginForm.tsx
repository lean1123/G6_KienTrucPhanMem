import React from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";

import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { login } from "../hooks/auth/authSlice";
import { unwrapResult } from "@reduxjs/toolkit";
import { enqueueSnackbar } from "notistack";
import { AppDispatch } from "../hooks/redux/store";

type Props = {};

const LoginForm: React.FC<Props> = () => {
  const dispatch = useDispatch<AppDispatch>();
  // const dispatch = useDispatch();
  const navigate = useNavigate();

  const schema = yup.object().shape({
    email: yup.string().email().required(),
    password: yup.string().required(),
  });

  const form = useForm({
    defaultValues: {
      email: "",
      password: "",
    },
    resolver: yupResolver(schema),
  });

  const handleOnSubmit = async (values: any) => {
    try {
      const result = await dispatch(login(values));
      const resultUnwrapped = unwrapResult(result);
      console.log("Result:", resultUnwrapped);

      if (resultUnwrapped?.code === 1000) {
        enqueueSnackbar("Đăng nhập thành công", { variant: "success" });
        navigate("/");
        return;
      }

      enqueueSnackbar("Đăng nhập thất bại", { variant: "error" });
    } catch (error) {
      console.log("Error: ", error);
      enqueueSnackbar("Đăng nhập thất bại", { variant: "error" });
    }
  };

  return (
    <div className="h-screen w-full flex justify-between items-center">
      <div className="flex justify-center w-full">
        <div className="boder-login p-5">
          <form
            className="flex-col items-start"
            onSubmit={form.handleSubmit(handleOnSubmit)}
          >
            <p className="pt-4 pb-5 text-sl font-bold">ĐĂNG NHẬP</p>
            <p className="pb-5 text-sl font-calibri">
              Nếu bạn có tài khoản, vui lòng đăng nhập.
            </p>
            <div className="w-full mb-4 search-container">
              <input
                className="w-full boder no-border py-1 px-20 input-field"
                type="email"
                id="email"
                placeholder="Email"
                {...form.register("email")}
              />
            </div>
            <div className="mb-4 search-container">
              <input
                type="password"
                id="password"
                placeholder="Password"
                className="w-full boder no-border py-1 px-20 input-field"
                {...form.register("password")}
              />
            </div>
            <div className="flex justify-center">
              <button
                type="submit"
                className="bg-red py-2 px-4 text-white hover:bg-black"
              >
                Đăng Nhập
              </button>
            </div>
            <div className="flex justify-center text-sl mt-3 hover:text-red-500">
              <button>Bạn quên mật khẩu?</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
