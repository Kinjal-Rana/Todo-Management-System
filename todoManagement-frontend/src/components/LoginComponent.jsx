import React from 'react'
import { useState } from 'react'
import { loginAPICall, saveLoggedInUser, storeToken } from '../services/AuthService'
import { useNavigate } from 'react-router-dom'

const LoginComponent = () => {

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')

    const navigator = useNavigate();

    //asynchronous - whenever we will get a response from a login REST API then it will call 
    //this : saveLoggedInUser(username) 
    async function handleLoginForm(e)
    {
        e.preventDefault();

        //await - wait for response from the login REST API and then it will execute the logic
        await loginAPICall(username, password).then((response) => {
            console.log(response.data);

            //const token = 'Basic '+ window.btoa(username + ":" + password);
            const token = 'Bearer ' + response.data.accessToken;
            const role = response.data.role;

            storeToken(token);

            saveLoggedInUser(username, role); //save the username in a session storage

            navigator("/todos")

            //once user navigated to the list of todos page, refresh the page
            window.location.reload(false); 
        }).catch(error => {
            console.error(error);
        })
    }

  return (
    <div className='container'>
        <br /><br />
        <div className='row'>
            <div className='col-md-6 offset-md-3'>
               
                <div className='card'>
                    <div className='card-header'>
                        <h2 className='text-center'>User Login Form</h2>
                    </div>

                    <div className='card-body'>
                        <form>

                        <div className='row mb-3'>
                                <label className='col-md-3 control-label'> Username or Email</label>
                                <div className='col-md-9'>
                                    <input type='text' 
                                        name='username' 
                                        className='form-control' 
                                        placeholder='Enter Username'
                                        value={username}
                                        onChange={ (e) => setUsername(e.target.value)}
                                    > </input>
                                </div>
                            </div>

                            <div className='row mb-3'>
                                <label className='col-md-3 control-label'> Password </label>
                                <div className='col-md-9'>
                                    <input type='password' 
                                        name='password' 
                                        className='form-control' 
                                        placeholder='Enter Password'
                                        value={password}
                                        onChange={ (e) => setPassword(e.target.value)}
                                    > </input>
                                </div>
                            </div>

                            <div className='form-group mb-3'>
                                <button className='btn btn-primary' onClick={ (e) => handleLoginForm(e)}> Submit </button>
                            </div>

                        </form>
                    </div>
                </div>

            </div>
        </div>
    </div>
  )
}

export default LoginComponent