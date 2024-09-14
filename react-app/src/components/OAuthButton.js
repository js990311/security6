import React from 'react';

const OAuthButton = () => {
    const handleLogin = () => {
        const url = 'http://localhost:8080/oauth2/authorization/keycloak';

        // fetch API를 사용하여 GET 요청 보내기
        fetch(url, {
            method: 'GET',
            credentials: 'include' // 필요한 경우 인증 정보를 포함
        })
            .then(response => {
                if (response.redirected) {
                    // 리다이렉션 처리
                    window.location.href = response.url;
                }
            })
            .catch(error => {
                console.error('Error during fetch:', error);
            });
    };

    return (
        <button onClick={handleLogin}>
            로그인
        </button>
    );
};

export default OAuthButton;
