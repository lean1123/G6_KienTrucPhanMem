import { EditOutlined } from "@ant-design/icons";
import {
  Add,
  DeleteForeverOutlined,
  VisibilityOutlined,
} from "@mui/icons-material";
import { enqueueSnackbar } from "notistack";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import colorApi from "../../api/colorApi";

interface Color {
  id: string;
  name: string;
  code: string;
}

// const colorData: color[] = [
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

const TableColor = () => {
  const navigate = useNavigate();
  const [colors, setColors] = useState<Color[]>([]);
  const [loading, setLoading] = useState(false);
  console.log(loading);

  const [keyword, setKeyword] = useState("");
  const fetchColor = async () => {
    setLoading(true);
    try {
      const response = await colorApi.getAllColors();
      console.log(response.data.result);
      setColors(response.data.result);
      if (response.data.code === 503) {
        enqueueSnackbar(response.data.message, { variant: "error" });
      }
    } catch (error) {
      console.error("Failed to fetch color:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterColors = async () => {
    setLoading(true);
    try {
      const response = await colorApi.searchColors(keyword);
      console.log(response.data.result);
      setColors(response.data.result);
      if (response.data.code === 503) {
        enqueueSnackbar(response.data.message, { variant: "error" });
      }
    } catch (error) {
      console.error("Failed to fetch color:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveColor = async (id: any) => {
    try {
      await colorApi.deleteColor(id);
      fetchColor();
      enqueueSnackbar("Remove color successfully", { variant: "success" });
    } catch (error) {
      console.error("Failed to remove color:", error);
      enqueueSnackbar("Remove color failed", { variant: "error" });
    }
  };

  useEffect(() => {
    fetchColor();
  }, []);
  return (
    <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
      <div className="py-6 px-4 md:px-6 xl:px-7">
        {/* <h4 className='text-xl font-semibold text-black'>List colors</h4> */}
        <div className="mt-2 flex items-center justify-between max-w-[400px] gap-4">
          <input
            type="text"
            className="w-full border border-gray-300 rounded-md py-2 px-4"
            placeholder="Search color..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button
            className="bg-black text-white px-6 py-2 rounded-md"
            onClick={() => filterColors()}
          >
            Search
          </button>
        </div>
      </div>

      <div className="max-w-full overflow-x-auto">
        <table className="w-full table-auto">
          <thead>
            <tr className="bg-gray-2 text-left">
              <th className="min-w-[220px] py-2 px-4 font-semibold text-black xl:pl-11">
                Color Name
              </th>
              <th className="py-2 px-4 font-semibold text-black">Actions</th>
            </tr>
          </thead>
          <tbody>
            {colors && colors.length > 0 &&
              colors.map((colorItem, key) => (
                <tr key={key}>
                  <td className="border-b border-[#eee] py-2 px-4 pl-9 xl:pl-11">
                    <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
                      <div
                        className={`p-4 rounded-full border border-[#eee] shadow-lg`}
                        style={{ backgroundColor: colorItem.code }}
                      ></div>
                      <p className="text-sm text-black">{colorItem.name}</p>
                    </div>
                  </td>

                  <td className="border-b border-[#eee] py-5 px-4">
                    <div className="flex items-center space-x-3.5">
                      {/* Edit button */}
                      <div className="relative group">
                        <button
                          className="hover:text-yellow-500"
                          onClick={() =>
                            navigate(`/colors/${colorItem.id}/edit`)
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
                          onClick={() => handleRemoveColor(colorItem.id)}
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

export default TableColor;
