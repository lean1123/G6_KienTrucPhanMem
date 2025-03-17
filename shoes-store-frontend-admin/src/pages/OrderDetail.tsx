import React, { useEffect, useState } from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import { useNavigate, useParams } from "react-router";
import orderApi from "../api/orderApi";
import orderDetailApi from "../api/orderDetailApi";

type OrderDetail = {
  id: number;
  image?: string;
  quantity: number;
  pricePerItem: number;
  productName?: string;
};

type Order = {
  id: number;
  customerName: string;
  createdDate: Date;
  totalPrice: number;
  paymentMethod: string;
  orderStatus: string;
  orderDetails: OrderDetail[];
  user?: {
    firstName: string;
    lastName: string;
  };
};

const initialOrderData: Order = {
  id: 12345,
  customerName: "John Doe",
  createdDate: new Date(),
  totalPrice: 199.99,
  paymentMethod: "Paypal",
  orderStatus: "Pending",
  orderDetails: [
    {
      id: 1,
      image:
        "https://th.bing.com/th/id/R.ed278c63783d1bfe0db837841c1cfc0e?rik=OCUTydZ3uoh%2b%2bg&pid=ImgRaw&r=0",
      productName: "Nike Air Max 270",
      quantity: 1,
      pricePerItem: 199.99,
    },
    {
      id: 2,
      image:
        "https://th.bing.com/th/id/R.ed278c63783d1bfe0db837841c1cfc0e?rik=OCUTydZ3uoh%2b%2bg&pid=ImgRaw&r=0",
      productName: "Adidas Yeezy Boost 350",
      quantity: 1,
      pricePerItem: 199.99,
    },
  ],
};

const OrderDetail = ({ isEdit }: { isEdit: boolean }) => {
  const navigate = useNavigate();
  const [orderData, setOrderData] = useState<Order>(initialOrderData);
  const [loading, setLoading] = useState(false);

  const [data, setData] = useState<Order>();
  const { id } = useParams();

  const fetchOrderData = async () => {
    setLoading(true);
    try {
      const response = await orderApi.getOrderById(id);
      setData(response.data.data);
      const orderDetailsResponse = await orderDetailApi.getOrderDetail(id);

      orderDetailsResponse.data.data.forEach((detail: any) => {
        detail.image = detail.productItem.listDetailImages[0];
        detail.productName = detail.productItem.product.name;
      });
      const combinedData: Order = {
        ...response.data.data,
        orderDetails: orderDetailsResponse.data.data,
      };

      setData(combinedData);
      console.log("Combined Data:", combinedData);
    } catch (error) {
      console.error("Failed to fetch order data:", error);
    } finally {
      setLoading(false);
    }
  };

  const updateQuantity = (id: number, increment: boolean) => {
    if (!data) return;
    const updatedOrderDetails = data.orderDetails.map((detail) =>
      detail.id === id
        ? {
            ...detail,
            quantity: Math.max(1, detail.quantity + (increment ? 1 : -1)),
          }
        : detail
    );
    setData({ ...data, orderDetails: updatedOrderDetails });
  };

  const handleSave = () => {
    // Xử lý logic lưu dữ liệu
    console.log("Order saved:", orderData);
  };

  const handleBack = () => {
    // Điều hướng hoặc xử lý logic quay lại
    navigate(-1);
    console.log("Back button clicked");
  };

  useEffect(() => {
    fetchOrderData();
  }, []);

  return (
    <>
      <Breadcrumb pageName="Order Detail" />
      <div className="grid grid-cols-1">
        {/* Order Information */}
        <div className="bg-white shadow-md rounded-lg p-6 mb-1">
          <h2 className="text-2xl font-semibold mb-4">Order Details</h2>
          <p className="text-lg mb-2">
            <strong>Order ID:</strong> {data?.id ? data.id : "N/A"}
          </p>
          <p className="text-lg mb-2">
            <strong>Customer Name:</strong>{" "}
            {data?.user?.firstName ? data.user.firstName : "N/A"}{" "}
            {data?.user?.lastName ? data.user.lastName : "N/A"}
          </p>
          <p className="text-lg mb-2">
            <strong>Order Date:</strong>{" "}
            {data?.createdDate ? data.createdDate.toString() : "N/A"}
          </p>
          <p className="text-lg mb-2">
            <strong>Total Amount:</strong>
            {/* {data?.totalPrice ? data.totalPrice : 'N/A'} */}
            {data &&
              data.orderDetails
                .reduce(
                  (total, item) => total + item.pricePerItem * item.quantity,
                  0
                )
                .toFixed(2)}
            VND
          </p>
          <p className="text-lg mb-2">
            <strong>Payment Method:</strong>{" "}
            {data?.paymentMethod ? data.paymentMethod : "N/A"}
          </p>
          <p className="text-lg mb-2">
            <strong>Status:</strong>{" "}
            {data?.orderStatus ? data.orderStatus : "N/A"}
          </p>
        </div>
        {/* Action Buttons */}
        <div className="flex justify-end my-2 space-x-4">
          <button
            onClick={handleBack}
            className="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400"
          >
            Back
          </button>
          {isEdit && (
            <button
              onClick={handleSave}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
              Save
            </button>
          )}
        </div>

        {/* Order Details List */}
        <div className="bg-white shadow-md rounded-lg p-6">
          <h2 className="text-2xl font-semibold mb-4">Order Items</h2>
          <table className="w-full table-auto">
            <thead>
              <tr className="border-b">
                <th className="py-2 text-left">Product</th>
                <th className="py-2 text-left">Quantity</th>
                <th className="py-2 text-left">Price</th>
                <th className="py-2 text-left">Total</th>
              </tr>
            </thead>
            <tbody>
              {data &&
                data.orderDetails.map((detail) => (
                  <tr key={detail.id} className="border-b">
                    <td className="py-2">
                      <div className="flex items-center space-x-4 gap-3">
                        <img
                          src={detail.image}
                          alt={detail.productName}
                          className="w-16 h-16 object-cover rounded-md"
                        />
                        {detail.productName}
                      </div>
                    </td>
                    <td className="py-2">
                      {isEdit ? (
                        <div className="flex items-center space-x-3">
                          <button
                            className="px-2 py-1 bg-gray-300 rounded"
                            onClick={() => updateQuantity(detail.id, false)}
                          >
                            -
                          </button>
                          <span>{detail.quantity}</span>
                          <button
                            className="px-2 py-1 bg-gray-300 rounded"
                            onClick={() => updateQuantity(detail.id, true)}
                          >
                            +
                          </button>
                        </div>
                      ) : (
                        <span>{detail.quantity}</span>
                      )}
                    </td>
                    <td className="py-2">{detail.pricePerItem}VND</td>
                    <td className="py-2">
                      {detail.pricePerItem * detail.quantity}VND
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default OrderDetail;
