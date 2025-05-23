import PropTypes from 'prop-types';
function MainLayout({ children }) {
	return <main>{children}</main>;
}

export default MainLayout;

MainLayout.propTypes = {
	children: PropTypes.node.isRequired,
};
