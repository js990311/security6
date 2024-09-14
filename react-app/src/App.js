import logo from './logo.svg';
import './App.css';
import { BrowserRouter } from 'react-router-dom';
import OAuthButton from './components/OAuthButton';
import BasicLogin from './components/BasicLogin';

function App() {
  return (
    <div className="App">
      <BasicLogin></BasicLogin>
    </div>
  );
}

export default App;
