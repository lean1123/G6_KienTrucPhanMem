import { Link } from "react-router-dom";

const Cart = () => {
  return (
    <div className="w-full flex h-full items-center justify-center">
      <Link
        to="/cart/pay"
        className="bg-slate-800 text-white rounded-lg p-2 text-4xl"
      >
        <strong className="hover:text-black">Go to pay</strong>
      </Link>
    </div>
  );
};

export default Cart;
