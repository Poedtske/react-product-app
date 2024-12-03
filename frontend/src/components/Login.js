import {React,useState} from 'react'
import{
    Card,
    CardContent,
    Typography,
    CardActions,
    Button,
    Divider,
    Grid,
    TextField,
    Container,
    Box
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function Login() {

    const [email, setEmail]=useState('');
    const [password, setPassword]=useState('');
    const [error, setError]=useState('');
    const navigate=useNavigate();

    const handleLogin = async(e)=>{
        e.preventDefault()

        setError('')

        const loginData={
            email,
            password
        }

        try{
            const response = await axios.post('http://localhost:8080/login',loginData);
            console.log(response)
            if (response.status===200){
                navigate('loginSuccessful');
            }else{
                const errorData=await response.json()
                setError(errorData.message|| 'Login failed for user. Please retry!');
            }
        }catch(err){
            setError('An error occured. Please retry')
        }
    }

  return (
    <Container
  maxWidth="xs"
  sx={{
    backgroundColor: "black",
    color: "white",
    borderRadius: 2,
    padding: 3,
    boxShadow: 3,
  }}
>
  <Box
    component="form"
    onSubmit={handleLogin}
    sx={{
      mt: 4,
      display: "flex",
      flexDirection: "column",
      gap: 2,
    }}
  >
    <Typography
      variant="h5"
      component="h1"
      align="center"
      sx={{ color: "white" }}
    >
      Login
    </Typography>

    {error && (
      <Typography
        variant="body2"
        color="error"
        align="center"
        sx={{ color: "red" }}
      >
        {error}
      </Typography>
    )}

    <TextField
      label="Email"
      variant="outlined"
      fullWidth
      value={email}
      onChange={(e) => setEmail(e.target.value)}
      required
      InputLabelProps={{
        style: { color: "white" },
      }}
      InputProps={{
        style: { color: "white", backgroundColor: "#333" },
      }}
    />

    <TextField
      label="Password"
      variant="outlined"
      type="password"
      fullWidth
      value={password}
      onChange={(e) => setPassword(e.target.value)}
      required
      InputLabelProps={{
        style: { color: "white" },
      }}
      InputProps={{
        style: { color: "white", backgroundColor: "#333" },
      }}
    />

    <Button
      type="submit"
      variant="contained"
      fullWidth
      sx={{
        backgroundColor: "white",
        color: "black",
        "&:hover": {
          backgroundColor: "gray",
          color: "white",
        },
      }}
    >
      Login
    </Button>
  </Box>
</Container>

  )
}
