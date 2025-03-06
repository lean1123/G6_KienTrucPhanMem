import { EditOutlined } from "@ant-design/icons";
import { DeleteForeverOutlined } from "@mui/icons-material";
import { CircularProgress } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import collectionApi from "../../api/collectionApi";

interface Collection {
  id: number;
  name: string;
  brandId: string;
}

const TableCollection = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [loading, setLoading] = React.useState(false);
  const [collections, setCollections] = React.useState<Collection[]>([]);
  const [keyword, setKeyword] = React.useState("");

  const fetchCollection = async () => {
    setLoading(true);
    try {
      const response = await collectionApi.getCollectionByBrand(id);
      console.log(response.data);
      setCollections(response.data);
    } catch (error) {
      console.error("Failed to fetch collections:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterCollection = async () => {
    setLoading(true);
    try {
      const response = await collectionApi.searchCollections(id, keyword);
      console.log(response.data);
      setCollections(response.data);
    } catch (error) {
      console.error("Failed to fetch collections:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: any) => {
    setLoading(true);
    try {
      await collectionApi.deleteCollection(id);
      fetchCollection();
      enqueueSnackbar("Delete collection successfully", { variant: "success" });
    } catch (error) {
      console.error("Failed to fetch collections:", error);
      enqueueSnackbar("Delete collection failed", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCollection();
  }, []);

  return (
    <>
      {loading ? (
        <div className="flex items-center justify-center">
          <CircularProgress />
        </div>
      ) : (
        <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
          <div className="py-6 px-4 md:px-6 xl:px-7 flex justify-between">
            {/* <h4 className='text-xl font-semibold text-black'>List Collections</h4> */}
            <div className="mt-2 flex items-center justify-between max-w-[400px] gap-4">
              <input
                type="text"
                className="w-full border border-gray-300 rounded-md py-2 px-4"
                placeholder="Search collection..."
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
              />
              <button
                className="bg-black text-white px-6 py-2 rounded-md"
                onClick={() => {
                  filterCollection();
                }}
              >
                Search
              </button>
            </div>
            <button
              className="bg-gray-500 text-white px-6 py-2 rounded-md"
              onClick={() => {
                navigate(-1);
              }}
            >
              Back
            </button>
          </div>

          <div className="max-w-full overflow-x-auto">
            <table className="w-full table-auto">
              <thead>
                <tr className="bg-gray-2 text-left">
                  <th className="min-w-[220px] py-2 px-4 font-medium text-black xl:pl-11">
                    Collection Name
                  </th>
                  <th className="py-2 px-4 font-medium text-black">Actions</th>
                </tr>
              </thead>
              <tbody>
                {collections.map((item, key) => (
                  <tr key={key}>
                    <td className="border-b border-[#eee] py-2 px-4 pl-9 xl:pl-11">
                      <p className="text-sm text-black">{item.name}</p>
                    </td>

                    <td className="border-b border-[#eee] py-2 px-4">
                      <div className="flex items-center space-x-3.5">
                        {/* Add button */}
                        {/* <div className='relative group'>
											<button
												className='hover:text-green-500'
												onClick={() => navigate('/admin/brands/add-collection')}
											>
												<Add className='w-5 h-5' />
											</button>
											<span className='absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap'>
												Add collection
											</span>
										</div> */}

                        {/* Edit button */}
                        <div className="relative group">
                          <button
                            className="hover:text-yellow-500"
                            onClick={() =>
                              navigate(
                                `/admin/brands/view-collection/${id}/${item.id}/edit`
                              )
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
      )}
    </>
  );
};

export default TableCollection;
