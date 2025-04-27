import {
  ArrowRightOutlined,
  FacebookOutlined,
  InstagramOutlined,
  MenuOutlined,
  PinterestOutlined,
  TikTokOutlined,
  TwitterOutlined,
  YoutubeOutlined,
} from "@ant-design/icons";
import { useState } from "react";
import { Link } from "react-router-dom";
function Footer() {
  const [isShow, setIsShow] = useState(true);
  const handleShow = () => {
    setIsShow(!isShow);
  };
  return (
    <div>
      <div className="bg-slate-800 p-4">
        <p className="text-2xl text-yellow-500 font-bold md:text-md">
          TRỞ THÀNH HỘI VIÊN & HƯỞNG ƯU ĐÃI 15%
          <Link
            to="/login"
            className="inline-block bg-white ml-10 px-4 py-2 rounded-md hover:bg-yellow-500 from-neutral-950 hover:text-slate-800 transition-colors hover:italic"
          >
            ĐĂNG KÝ MIỄN PHÍ <ArrowRightOutlined className="text-2xl" />
          </Link>
        </p>
      </div>
      <button
        onClick={handleShow}
        className="bg-white p-4 text-white md:hidden"
      >
        <MenuOutlined className="text-3xl text-slate-800" />
      </button>
      {isShow && (
        <div className="bg-white flex flex-col justify-around mt-10 md:flex-row">
          <div className="flex flex-col space-y-2.5 md:flex-col">
            <strong>THƯƠNG HIỆU</strong>
            <Link
              to="/products/giayConverse"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày Converse
            </Link>
            <Link
              to="/products/giayVans"
              className="block hover:italic hover:text-slate-500 transition-colors "
            >
              Giày Vans
            </Link>
            <Link
              to="/products/giayAdidas"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày Adidas
            </Link>
            <Link
              to="/products/giayNike"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày Nike
            </Link>
            <Link
              to="/products/giayPuma"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày Puma
            </Link>
          </div>
          <div className="flex flex-col space-y-2.5 md:flex-col">
            <strong>SẢN PHẨM</strong>
            <Link
              to="/products/giayCoCao"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày cổ cao
            </Link>
            <Link
              to="/products/giayCoThap"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày cổ thấp
            </Link>
            <Link
              to="/products/giayTheThao"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày thể thao
            </Link>
            <Link
              to="/products/giayNam"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày nam
            </Link>
            <Link
              to="/products/giayNu"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giày nữ
            </Link>
          </div>
          <div className="flex flex-col space-y-2.5 md:flex-col">
            <strong>THỂ THAO</strong>
            <Link
              to="/products/chayBo"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Chạy bộ
            </Link>
            <Link
              to="/products/tennis"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Tennis
            </Link>
            <Link
              to="/products/bongRo"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Bóng rổ
            </Link>
            <Link
              to="/products/bongDa"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Bóng đá
            </Link>
            <Link
              to="/products/yoga"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Yoga
            </Link>
          </div>
          <div className="flex flex-col space-y-2.5 md:flex-col">
            <strong>THÔNG TIN VỀ CÔNG TY</strong>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giới Thiệu Về Chúng Tôi
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Cơ Hội Nghề Nghiệp
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Tin tức{" "}
            </Link>
          </div>
          <div className="flex flex-col space-y-2.5 md:flex-col">
            <strong>HỖ TRỢ</strong>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Trợ Giúp
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Công cụ tìm kiếm cửa hàng{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Biểu Đồ Kích Cỡ{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Thanh Toán{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Giao Hàng{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Trả hàng & Hoàn tiền{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Khuyến mãi{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Sơ đồ trang web{" "}
            </Link>
            <Link
              to="/home"
              className="block hover:italic hover:text-slate-500 transition-colors"
            >
              Trợ giúp dịch vụ khách hàng{" "}
            </Link>
            <Link to="http://online.gov.vn/Home/WebDetails/42430?AspxAutoDetectCookieSupport=1">
              <img
                src="https://brand.assets.adidas.com/image/upload/f_auto,q_auto,fl_lossy/vn_footer_logo_1_2d3cf77993.jpg"
                alt="adidas"
              />
            </Link>
          </div>
          <div className="flex flex-col space-y-4 md:flex-col">
            <strong>THEO DÕI CHÚNG TÔI</strong>
            <Link to="https://www.facebook.com/thinnnger">
              <FacebookOutlined className="text-2xl" />
            </Link>
            <Link to="https://www.facebook.com/thinnnger">
              <InstagramOutlined className="text-2xl" />
            </Link>
            <Link to="https://www.facebook.com/thinnnger">
              <TwitterOutlined className="text-2xl" />
            </Link>
            <Link to="https://www.facebook.com/thinnnger">
              <PinterestOutlined className="text-2xl" />
            </Link>
            <Link to="https://www.facebook.com/thinnnger">
              <TikTokOutlined className="text-2xl" />
            </Link>
            <Link to="https://www.facebook.com/thinnnger">
              <YoutubeOutlined className="text-2xl" />
            </Link>
          </div>
        </div>
      )}
    </div>
  );
}

export default Footer;
