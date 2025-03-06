import { EditOutlined } from "@ant-design/icons";
import {
  Add,
  DeleteForeverOutlined,
  VisibilityOutlined,
} from "@mui/icons-material";
import { enqueueSnackbar } from "notistack";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import brandApi from "../../api/brandApi";

interface Brand {
  id: number;
  brandName: string;
  avatar: string;
}

// const brandData: Brand[] = [
// 	{
// 		id: 1,
// 		name: 'Nike',
// 		image:
// 			'https://th.bing.com/th/id/R.341824fb9731186e574fe00ab9a5da66?rik=uFs84f4Clc%2b7bw&pid=ImgRaw&r=0',
// 	},
// 	{
// 		id: 2,
// 		name: 'Adidas',
// 		image:
// 			'https://th.bing.com/th/id/R.341824fb9731186e574fe00ab9a5da66?rik=uFs84f4Clc%2b7bw&pid=ImgRaw&r=0',
// 	},
// 	{
// 		id: 3,
// 		name: 'Puma',
// 		image:
// 			'https://th.bing.com/th/id/R.341824fb9731186e574fe00ab9a5da66?rik=uFs84f4Clc%2b7bw&pid=ImgRaw&r=0',
// 	},
// 	{
// 		id: 4,
// 		name: 'Reebok',
// 		image:
// 			'https://th.bing.com/th/id/R.341824fb9731186e574fe00ab9a5da66?rik=uFs84f4Clc%2b7bw&pid=ImgRaw&r=0',
// 	},
// ];

const TableBrand = () => {
  const navigate = useNavigate();
  const [brands, setBrands] = useState<Brand[]>([]);
  const [loading, setLoading] = useState(false);
  console.log(loading);

  const [keyword, setKeyword] = useState("");
  const fetchBrand = async () => {
    setLoading(true);
    try {
      const response = await brandApi.getAllBrands();
      console.log(response.data);
      setBrands(response.data);
    } catch (error) {
      console.error("Failed to fetch brand:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterBrands = async () => {
    setLoading(true);
    try {
      const response = await brandApi.searchBrands(keyword);
      console.log(response.data);
      setBrands(response.data);
    } catch (error) {
      console.error("Failed to fetch brand:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveBrand = async (id: any) => {
    try {
      await brandApi.deleteBrand(id);
      fetchBrand();
      enqueueSnackbar("Remove brand successfully", { variant: "success" });
    } catch (error) {
      console.error("Failed to remove brand:", error);
      enqueueSnackbar("Remove brand failed", { variant: "error" });
    }
  };

  useEffect(() => {
    fetchBrand();
  }, []);
  return (
    <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
      <div className="py-6 px-4 md:px-6 xl:px-7">
        {/* <h4 className='text-xl font-semibold text-black'>List Brands</h4> */}
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
            onClick={() => filterBrands()}
          >
            Search
          </button>
        </div>
      </div>

      <div className="max-w-full overflow-x-auto">
        <table className="w-full table-auto">
          <thead>
            <tr className="bg-gray-2 text-left">
              <th className="min-w-[220px] py-2 px-4 font-medium text-black xl:pl-11">
                Brand Name
              </th>
              <th className="py-2 px-4 font-medium text-black">Actions</th>
            </tr>
          </thead>
          <tbody>
            {brands.map((brandItem, key) => (
              <tr key={key}>
                <td className="border-b border-[#eee] py-2 px-4 pl-9 xl:pl-11">
                  <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
                    <div className="h-12 w-16 rounded-md">
                      <img
                        src={brandItem.avatar}
                        alt="Product"
                        className="h-12 w-16 rounded-md"
                      />
                    </div>
                    <p className="text-sm text-black">{brandItem.brandName}</p>
                  </div>
                </td>

                <td className="border-b border-[#eee] py-5 px-4">
                  <div className="flex items-center space-x-3.5">
                    {/* Add view collection */}
                    <div className="relative group">
                      <button
                        className="hover:text-blue-500"
                        onClick={() =>
                          navigate(
                            `/admin/brands/view-collection/${brandItem.id}`
                          )
                        }
                      >
                        <VisibilityOutlined className="w-5 h-5" />
                      </button>
                      <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                        View collection
                      </span>
                    </div>

                    {/* Add button */}
                    <div className="relative group">
                      <button
                        className="hover:text-green-500"
                        onClick={() =>
                          navigate(
                            `/admin/brands/add-collection/${brandItem.id}`
                          )
                        }
                      >
                        <Add className="w-5 h-5" />
                      </button>
                      <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                        Add collection
                      </span>
                    </div>

                    {/* Edit button */}
                    <div className="relative group">
                      <button
                        className="hover:text-yellow-500"
                        onClick={() =>
                          navigate(`/admin/brands/${brandItem.id}/edit`)
                        }
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
                        onClick={() => handleRemoveBrand(brandItem.id)}
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

export default TableBrand;
