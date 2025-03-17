import { EditOutlined } from "@ant-design/icons";
import { DeleteForeverOutlined, VisibilityOutlined } from "@mui/icons-material";
import { enqueueSnackbar } from "notistack";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import userApi from "../../api/userApi";

interface User {
  id: string;
  firstName: string;
  lastName: string;
  phone: string;
  avatar: string;
  gender: string;
  email?: string;
}

const TableUser = () => {
  const [loading, setLoading] = React.useState(false);
  const navigate = useNavigate();
  const [users, setUsers] = React.useState<User[]>([]);
  const [keyword, setKeyword] = React.useState("");
  const fetchUser = async () => {
    setLoading(true);
    try {
      const response = await userApi.getAll();
      console.log(response.data);
      const users = response.data;

      const accountWithUser = await Promise.all(
        users.map(async (user: User) => {
          if (user.id) {
            const accountResponse = await userApi.getAccount(user.id);
            return {
              ...user,
              email: accountResponse.data.email,
            };
          } else {
            // If no collection or brandId exists, handle accordingly (e.g., return product as is)
            return {
              ...user,
              email: "Unknown", // Or any fallback value
            };
          }
        })
      );

      setUsers(accountWithUser);
    } catch (error) {
      console.error("Failed to fetch product:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterUser = async () => {
    setLoading(true);
    try {
      const response = await userApi.search(keyword);
      console.log(response.data);
      setUsers(response.data);
    } catch (error) {
      console.error("Failed to fetch product:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await userApi.delete(id);
      const newUsers = users.filter((item) => item.id !== id);
      setUsers(newUsers);
      enqueueSnackbar("User deleted successfully!", { variant: "success" });
    } catch (error) {
      console.error("Failed to delete user:", error);
      enqueueSnackbar("Failed to delete user!", { variant: "error" });
    }
  };

  useEffect(() => {
    fetchUser();
  }, []);

  return (
    <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
      <div className="py-6 px-4 md:px-6 xl:px-7">
        {/* <h4 className='text-xl font-semibold text-black'>List Products</h4> */}
        <div className="mt-2 flex items-center justify-between max-w-[400px] gap-4">
          <input
            type="text"
            className="w-full border border-gray-300 rounded-md py-2 px-4"
            placeholder="Search brand..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button
            className="bg-black text-white px-6 py-2 rounded-md"
            onClick={() => {
              filterUser();
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
              <th className="min-w-[120px] py-2 px-4 font-medium text-black xl:pl-11">
                Avatar
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                First Name
              </th>
              <th className="min-w-[150px] py-2 px-4 font-medium text-black">
                Last Name
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                Email
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                Phone
              </th>
              <th className="min-w-[120px] py-2 px-4 font-medium text-black">
                Gender
              </th>
              <th className="py-2 px-4 font-medium text-black">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((item, key) => (
              <tr key={key}>
                <td className="border-b border-[#eee] py-2 px-4 pl-9 xl:pl-11">
                  <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
                    <div className="h-12 w-16 rounded-md">
                      <img
                        src={
                          item?.avatar ||
                          "https://th.bing.com/th/id/R.b1edc35f0fa63d0e0b3c9bc5537de942?rik=CZKF5DOfyXPxig&pid=ImgRaw&r=0"
                        }
                        alt="Product"
                        className="h-12 w-16 rounded-md"
                      />
                    </div>
                    {/* <p className='text-sm text-black'>{item.name}</p> */}
                  </div>
                </td>
                <td className="border-b border-[#eee] py-5 px-4">
                  <p className="text-black dark:text-white">
                    {item.firstName.length > 15
                      ? item.firstName.substring(0, 15) + "..."
                      : item.firstName}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4">
                  <p className="text-black dark:text-white">
                    {item.lastName.length > 15
                      ? item.lastName.substring(0, 15) + "..."
                      : item.lastName}
                  </p>
                </td>

                <td className="border-b border-[#eee] py-5 px-4">
                  <p className="text-black dark:text-white">
                    {item.email ? item.email : "Unknown"}
                  </p>
                </td>

                <td className="border-b border-[#eee] py-5 px-4">
                  <p className="text-black dark:text-white">
                    {item.phone ? item.phone : "Unknown"}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4">
                  <p className="text-black dark:text-white">
                    {item.gender ? item.gender : "Unknown"}
                  </p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4">
                  <div className="flex items-center space-x-3.5">
                    {/* Add view item */}
                    <div className="relative group">
                      <button
                        className="hover:text-blue-500"
                        onClick={() => navigate(`/admin/users/${item.id}`)}
                      >
                        <VisibilityOutlined className="w-5 h-5" />
                      </button>
                      <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                        View
                      </span>
                    </div>

                    {/* Edit button */}
                    <div className="relative group">
                      <button
                        className="hover:text-yellow-500"
                        onClick={() => navigate(`/admin/users/${item.id}/edit`)}
                      >
                        <EditOutlined className="w-5 h-5" />
                      </button>
                      <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                        Edit
                      </span>
                    </div>

                    {/* Delete button */}
                    <div className="relative group">
                      <button
                        className="hover:text-red-500"
                        onClick={() => handleDelete(item.id)}
                      >
                        <DeleteForeverOutlined className="w-5 h-5" />
                      </button>
                      <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                        Remove
                      </span>
                    </div>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TableUser;
