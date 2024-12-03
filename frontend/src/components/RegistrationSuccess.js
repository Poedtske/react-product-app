import React from 'react'
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

export default function RegistrationSuccess() {
  return (
    <Container maxWidth="xs">
        <Box sx={{ mt: 8, color:'green' }}>
            <Typography variant="h3" align="center" gutterBottom>
                You have sucessfully Registered. You can now login!
            </Typography>
            <Typography variant="h6" align="center" gutterBottom>
                <a href="/login">You can now login</a>
            </Typography>
        </Box>
    </Container>
  )
}
