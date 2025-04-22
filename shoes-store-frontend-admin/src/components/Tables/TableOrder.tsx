import { EditOutlined, VisibilityOutlined } from "@mui/icons-material";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import orderApi from "../../api/orderApi";

interface Order {
  id: string;
  user: {
    id: number;
    firstName: string;
    lastName: string;
  };
  totalPrice: number;
  orderStatus: string;
  createdDate: Date;
  paymentMethod: string;
}

// const orderData: Order[] = [
// 	{
// 		id: 1,
// 		user: {
// 			id: 1,
// 			name: 'John Doe',
// 		},
// 		totalPrice: 100,
// 		status: 'Delivered',
// 		createdAt: new Date(),
// 		payment: 'Paypal',
// 	},
// 	{
// 		id: 2,
// 		user: {
// 			id: 2,
// 			name: 'Jane Doe',
// 		},
// 		totalPrice: 200,
// 		status: 'Pending',
// 		createdAt: new Date(),
// 		payment: 'Stripe',
// 	},
// 	{
// 		id: 3,
// 		user: {
// 			id: 3,
// 			name: 'John Smith',
// 		},
// 		totalPrice: 300,
// 		status: 'Delivered',
// 		createdAt: new Date(),
// 		payment: 'Paypal',
// 	},
// 	{
// 		id: 4,
// 		user: {
// 			id: 4,
// 			name: 'Jane Smith',
// 		},
// 		totalPrice: 400,
// 		status: 'Pending',
// 		createdAt: new Date(),
// 		payment: 'Stripe',
// 	},
// ];

function TableOrder() {
  const navigate = useNavigate();
  const [orderData, setOrderData] = React.useState<Order[]>([]);
  const [loading, setLoading] = React.useState(false);
  const [keyword, setKeyword] = React.useState("");
  const fetchOrder = async () => {
    // Fetch order data here
    setLoading(true);
    try {
      const response = await orderApi.getAll();
      console.log(response.data);
      setOrderData(response.data.data);
    } catch (error) {
      console.error("Failed to fetch order:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterOrder = async () => {
    setLoading(true);
    try {
      const response = await orderApi.search(keyword);
      console.log(response.data);
      setOrderData(response.data.data);
    } catch (error) {
      console.error("Failed to fetch order:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrder();
  }, []);
  return (
    <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
      <div className="py-6 px-4 md:px-6 xl:px-7">
        {/* <h4 className='text-xl font-semibold text-black'>List Orders</h4> */}
        <div className="mt-2 flex items-center justify-between max-w-[400px] gap-4">
          <input
            type="text"
            className="w-full border border-gray-300 rounded-md py-2 px-4"
            placeholder="Search order..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button
            className="bg-black text-white px-6 py-2 rounded-md"
            onClick={() => {
              filterOrder();
            }}
          >
            Search
          </button>
        </div>
      </div>

      <div className="max-w-full overflow-x-auto">
        <table className="w-full table-auto">
          <thead>
            <tr className="bg-gray-2 text-left">
              <th className="min-w-[150px] py-2 px-4 font-medium text-black xl:pl-11">
                ID
              </th>
              <th className="min-w-[150px] py-2 px-4 font-medium text-black">
                User
              </th>
              <th className="min-w-[150px] py-2 px-4 font-medium text-black">
                Create at
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                Payment
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                Total Price
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                Status
              </th>
              <th className="py-2 px-4 font-medium text-black">Actions</th>
            </tr>
          </thead>
          <tbody>
            {orderData.map((item, key) => (
              <tr key={key}>
                <td className="border-b border-[#eee] py-2 px-4 pl-9 xl:pl-11">
                  <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
                    <p className="text-sm text-black">{item.id}</p>
                  </div>
                </td>
                <td className="border-b border-[#eee] py-2 px-4">
                  <p className="text-black dark:text-white">
                    {item.user.firstName}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-2 px-4">
                  <p className="text-black dark:text-white">
                    {item.createdDate.toString()}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-2 px-4">
                  <p className="text-black dark:text-white">
                    {item.paymentMethod}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-2 px-4">
                  <p className="text-black dark:text-white">
                    ${item.totalPrice}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-2 px-4">
                  <p className="text-black dark:text-white">
                    {item.orderStatus}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-2 px-4">
                  <div className="flex items-center space-x-3.5">
                    {/* Add button */}
                    <div className="relative group">
                      <button
                        className="hover:text-blue-500"
                        onClick={() => navigate(`/admin/orders/${item.id}`)}
                      >
                        <VisibilityOutlined className="w-5 h-5" />
                      </button>
                      <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                        View detail
                      </span>
                    </div>

                    {/* Edit button */}
                    {item?.orderStatus === "PENDING" && (
                      <div className="relative group">
                        <button
                          className="hover:text-yellow-500"
                          onClick={() =>
                            navigate(`/admin/orders/${item.id}/edit`)
                          }
                        >
                          <EditOutlined className="w-5 h-5" />
                        </button>
                        <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                          Edit
                        </span>
                      </div>
                    )}

                    {/* Delete button */}
                    {/* <div className='relative group'>
											<button className='hover:text-red-500'>
												<DeleteForeverOutlined className='w-5 h-5' />
											</button>
											<span className='absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap'>
												Remove
											</span>
										</div> */}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default TableOrder;
