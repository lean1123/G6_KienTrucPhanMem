import { Link } from "@mui/material";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../../hooks/auth/authSlice";
import { useNavigate } from "react-router";
import "@fortawesome/fontawesome-free/css/all.min.css";

function Header() {
  const [isOpen, setIsOpen] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [isOpendDiv, setIsOpenDiv] = useState(false);
  const [name, setName] = useState("TRẢ HÀNG DỄ DÀNG");

  const token = useSelector(
    (state) => state.persistedReducer?.user?.accessToken
  );

  useEffect(() => {
    if (token) {
      setIsOpen(false);
    }
  }, [token]);

  const handleLogout = () => {
    const action = logout();
    dispatch(action);
    navigate("/");
  };

  //set time
  useEffect(() => {
    const interval = setInterval(() => {
      setName((prevName) =>
        prevName === "TRẢ HÀNG DỄ DÀNG"
          ? "GIAO HÀNG MIỄN PHÍ CHO THÀNH VIÊN CỦA LENDOM"
          : "TRẢ HÀNG DỄ DÀNG"
      );
    }, 3000); // Đổi tên mỗi 3 giây

    // Clear interval khi component unmount để tránh lỗi rò rỉ bộ nhớ
    return () => clearInterval(interval);
  }, []);

  const toggleDropdown = () => {
    setIsOpenDiv(!isOpendDiv);
  };

  return (
    <>
      <div
        className="bg-black h-10 text-white flex justify-center items-center cursor-pointer"
        onClick={toggleDropdown}
      >
        <p className="text-xs font-medium leading-loose">{name}</p>
        <button className="bg-black text-white font-bold py-1 px-2 rounded">
          <i className="fas fa-chevron-down"></i>
        </button>
      </div>
      {isOpendDiv && (
        <div
          className="bg-white h-3/5 w-full fixed top-0 left-0 z-50 transition-transform transform duration-3000 ease-out"
          style={{
            overflow: "hidden",
            transform: isOpendDiv ? "translateY(0)" : "translateY(-100%)",
          }}
        >
          <button
            className="text-2xl font-serif text-black border border-black w-10 h-10 flex items-center justify-center fixed right-4 top-4"
            onClick={toggleDropdown}
          >
            ×
          </button>

          <div className="flex mt-24 ml-40">
            <div className="mr-20">
              <p className="text-sl font-calibri tracking-tight  font-bold">
                GIAO HÀNG MIỄN PHÍ CHO THÀNH VIÊN CỦA LENDOM
              </p>
              <div className="flex flex-col mt-2 text-sm">
                <div className="font-calibri text-sl">
                  Đăng ký thành viên LENDOM để hưởng thụ dịch vụ giao hàng
                </div>
                <div className="font-calibri">
                  miễn phí! Hoặc bạn chỉ được nhận ưu đãi miễn phí giao hàng với
                </div>
                <div className="font-calibri">
                  hóa đơn có trị giá ít nhất 1.6 triệu đồng
                </div>
                <Link class="text-base font-calibri font-bold text-black hover:underline mt-5">
                  THAM GIA NGAY
                </Link>
              </div>
            </div>
            <div className="">
              <p className="text-sl font-calibri tracking-tight  font-bold">
                TRẢ HÀNG DỄ DÀNG
              </p>
              <div className="flex flex-col mt-2 text-sm">
                <div className="font-calibri text-sl">
                  Nếu bạn không hài lòng với đơn hàng của mình, bạn có thể
                </div>
                <div className="font-calibri text-sl">
                  được hoàn lại tiền. Vui lòng xem Chính Sách Trả Hàng của chúng
                </div>
                <div className="font-calibri text-sl">
                  tôi để biết thêm chi tiết.
                </div>
                <Link class="text-base font-calibri font-bold text-black hover:underline mt-5">
                  TRẢ HÀNG DỄ DÀNG
                </Link>
              </div>
            </div>
          </div>
        </div>
      )}

      <nav className="bg-slate-100">
        <div className="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
          <div className="relative flex h-16 items-center justify-between">
            <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
              <div className="flex-shrink-0">
                <img
                  className="block h-11 w-auto"
                  src="/logo.png"
                  alt="Logo"
                  width={32}
                  height={32}
                />
              </div>
              <div className="hidden sm:ml-6 sm:block">
                <div className="flex space-x-4">
                  <Link
                    href="/"
                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                  >
                    Trang Chủ
                  </Link>
                  <Link
                    href="/about"
                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                  >
                    Về Chúng Tôi
                  </Link>
                  <Link
                    href="/products"
                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                  >
                    Sản Phẩm
                  </Link>
                  <Link
                    href="/categories"
                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                  >
                    Danh Mục
                  </Link>
                  <Link
                    href="/categoryforBoy"
                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                  >
                    Nam
                  </Link>
                  <Link
                    href="/categoryforGirl"
                    className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                  >
                    Nữ
                  </Link>
                </div>
              </div>
            </div>
            <div className="relative ml-3">
              <div>
                {token ? (
                  <button
                    type="button"
                    className="flex rounded-full bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"
                    id="user-menu-button"
                    aria-expanded={isOpen}
                    aria-haspopup="true"
                    onClick={() => setIsOpen(!isOpen)}
                  >
                    <img
                      className="h-8 w-8 rounded-full"
                      src="/defaultAvatar.jpg"
                      alt="user"
                      width={32}
                      height={32}
                    />
                  </button>
                ) : (
                  <>
                    <Link
                      href="/login"
                      className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                      Đăng Nhập
                    </Link>
                    <Link
                      href="/signup"
                      className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
                    >
                      Đăng Ký
                    </Link>
                  </>
                )}
              </div>
              {isOpen && token && (
                <div
                  className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
                  role="menu"
                  aria-orientation="vertical"
                  aria-labelledby="user-menu-button"
                  tabIndex={-1}
                >
                  <a
                    href="#"
                    className="block px-4 py-2 text-sm text-gray-700 hover:cursor-pointer hover:bg-gray-300"
                    role="menuitem"
                    tabIndex={-1}
                    id="user-menu-item-0"
                  >
                    Thông Tin Của Tôi
                  </a>
                  <a
                    href="#"
                    className="block px-4 py-2 text-sm text-gray-700 hover:cursor-pointer hover:bg-gray-300"
                    role="menuitem"
                    tabIndex={-1}
                    id="user-menu-item-1"
                  >
                    Chỉnh Sửa
                  </a>
                  <a
                    className="block px-4 py-2 text-sm text-gray-700 hover:cursor-pointer hover:bg-gray-300"
                    role="menuitem"
                    tabIndex={-1}
                    id="user-menu-item-2"
                    onClick={handleLogout}
                  >
                    Đăng Xuất
                  </a>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>
    </>
  );
}

export default Header;
