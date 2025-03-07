import { Link } from "react-router-dom";
interface BreadcrumbProps {
  pageName: string;
}
const Breadcrumb = ({ pageName }: BreadcrumbProps) => {
  return (
    <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
      <h2 className="text-xl font-semibold text-black">{pageName}</h2>

      <nav>
        <ol className="flex items-center gap-2">
          <li>
            <Link className="font-medium" to="/admin">
              Dashboard /
            </Link>
          </li>
          <li className="font-medium text-blue-600">{pageName}</li>
        </ol>
      </nav>
    </div>
  );
};

export default Breadcrumb;
