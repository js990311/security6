import { Link } from "react-router-dom";

export default function HomeComponent(){
    return (
        <div>
            <ul>
                <li>
                    <Link to="/login">로그인</Link>
                </li>
                <li>
                    <Link to="/need-auth">인증필요페이지</Link>
                </li>
            </ul>
        </div>
    );
}