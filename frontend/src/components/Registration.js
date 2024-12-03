import {React, useState} from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
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

export default function Registration(){

    const[formData, setFormData]=useState({
        firstName:'',
        lastName:'',
        email:'',
        password:'',
        confirmPassword:''
    })

    const [error, setError]=useState('')
    const navigate=useNavigate()

    const handleChange=(e)=>{
        setFormData({
            ...formData,
            [e.target.name]:e.target.value
        });
    };

    const handleSubmit = async (e)=>{
        e.preventDefault();

        if(formData.password!=formData.confirmPassword){
            setError('Passwords do not match');
            return;
        }
        setError('')

        try{
            const response = await axios.post('http://localhost:8080/register', formData);
            if (response.status === 201){
                navigate('/registrationSuccessful')
            }else{
                const errorText= await response.txt();
                setError(errorText);
            }
        }catch(err){
            setError('An error occured during user registration')
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
        <Box sx={{ mt: 8 }}>
        <Typography variant="h5" align="center" gutterBottom sx={{ color: "white" }}>
            Register
        </Typography>

        <form onSubmit={handleSubmit}>
            <Grid container spacing={2}>
            <Grid item xs={12}>
                <TextField
                label="First Name"
                name="firstName"
                fullWidth
                value={formData.firstName}
                onChange={handleChange}
                required
                InputLabelProps={{
                    style: { color: "white" },
                }}
                InputProps={{
                    style: { color: "white", backgroundColor: "#333" },
                }}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                label="Last Name"
                name="lastName"
                fullWidth
                value={formData.lastName}
                onChange={handleChange}
                required
                InputLabelProps={{
                    style: { color: "white" },
                }}
                InputProps={{
                    style: { color: "white", backgroundColor: "#333" },
                }}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                label="Email"
                name="email"
                fullWidth
                value={formData.email}
                onChange={handleChange}
                required
                InputLabelProps={{
                    style: { color: "white" },
                }}
                InputProps={{
                    style: { color: "white", backgroundColor: "#333" },
                }}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                label="Password"
                name="password"
                type="password"
                fullWidth
                value={formData.password}
                onChange={handleChange}
                required
                InputLabelProps={{
                    style: { color: "white" },
                }}
                InputProps={{
                    style: { color: "white", backgroundColor: "#333" },
                }}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                label="Confirm Password"
                name="confirmPassword"
                type="password"
                fullWidth
                value={formData.confirmPassword}
                onChange={handleChange}
                required
                InputLabelProps={{
                    style: { color: "white" },
                }}
                InputProps={{
                    style: { color: "white", backgroundColor: "#333" },
                }}
                />
            </Grid>
            {error && (
                <Grid item xs={12}>
                <Typography color="error" variant="body2" sx={{ color: "red" }}>
                    {error}
                </Typography>
                </Grid>
            )}
            <Grid item xs={12}>
                <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{
                    backgroundColor: "white",
                    color: "black",
                    "&:hover": {
                    backgroundColor: "gray",
                    color: "white",
                    },
                }}
                >
                Register
                </Button>
            </Grid>
            </Grid>
        </form>
        </Box>
    </Container>
    );
}