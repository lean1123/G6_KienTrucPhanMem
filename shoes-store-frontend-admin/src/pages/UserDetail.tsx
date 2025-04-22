import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import userApi from "../api/userApi";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

interface User {
  id: string;
  firstName: string;
  lastName: string;
  phone: string;
  avatar: string;
  gender: string;
  email?: string;
}

function UserDetail() {
  const { id } = useParams();
  const [user, setUser] = React.useState<User | null>(null);
  const navigate = useNavigate();
  const fetchUser = async () => {
    try {
      const response = await userApi.getById(id);
      console.log(response.data);
      const user = response.data;
      const accountResponse = await userApi.getAccount(user.id);
      setUser({ ...user, email: accountResponse.data.email });
    } catch (error) {
      console.error("Failed to fetch user:", error);
    }
  };

  useEffect(() => {
    fetchUser();
  }, []);
  return (
    <>
      <Breadcrumb pageName="User Detail" />

      <div className="flex flex-col gap-10">
        <div className="bg-white shadow-md rounded-lg p-6 flex items-center space-x-6 flex-col">
          {/* Info */}
          <h2 className="text-2xl font-semibold text-gray-800 float-start w-full">
            Information
          </h2>
          <div className="flex flex-col justify-center items-center gap-2">
            <img
              src={
                user?.avatar ||
                "https://th.bing.com/th/id/R.b1edc35f0fa63d0e0b3c9bc5537de942?rik=CZKF5DOfyXPxig&pid=ImgRaw&r=0"
              }
              alt={`${user?.firstName} ${user?.lastName}`}
              className="w-24 h-24 rounded-full object-cover"
            />
            <h2 className="text-xl font-bold text-gray-800">{`${user?.firstName} ${user?.lastName}`}</h2>
            <p className="text-gray-600">
              <span className="font-medium">Gender:</span>{" "}
              {user?.gender ? user.gender : "Unknown"}
            </p>
            <p className="text-gray-600">
              <span className="font-medium">Phone:</span>{" "}
              {user?.phone ? user.phone : "Unknown"}
            </p>
            {user?.email && (
              <p className="text-gray-600">
                <span className="font-medium">Email:</span> {user.email}
              </p>
            )}
            <div className="flex gap-4">
              <button
                className="bg-blue-500 text-white px-4 py-2 rounded-md"
                onClick={() => navigate(-1)}
              >
                Back to users
              </button>
              <button
                className="bg-orange-500 text-white px-4 py-2 rounded-md"
                onClick={() => navigate(`/admin/users/${id}/edit`)}
              >
                Edit
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default UserDetail;
