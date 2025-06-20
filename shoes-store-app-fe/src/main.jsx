import { SnackbarProvider } from 'notistack';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { PersistGate } from 'redux-persist/integration/react';
import App from './App.jsx';
import { persistor, store } from './hooks/redux/store.js';
import './index.css';
import './resource/resources.js';

createRoot(document.getElementById('root')).render(
	// 	<StrictMode>
	<Provider store={store}>
		<PersistGate
			loading={
				<div className='w-full flex-row justify-center'>
					<h2>Đang tải dữ liệu...</h2>
				</div>
			}
			persistor={persistor}
		>
			<BrowserRouter>
				<SnackbarProvider
					maxSnack={3}
					anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
					autoHideDuration={3000}
					style={{ zIndex: 99999 }}
				>
					<App />
				</SnackbarProvider>
			</BrowserRouter>
		</PersistGate>
	</Provider>,
	// 	</StrictMode>,
);
