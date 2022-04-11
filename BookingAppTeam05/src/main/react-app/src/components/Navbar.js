import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import {useHistory} from "react-router-dom";

const pages = ['Cottage Profile', 'Add Cottage', 'Adventure Profile', 'Add adventure'];
const settings = ['Profile', 'Edit Profile', 'Change Password', 'Logout'];

const Navbar = () => {
  const [anchorElNav, setAnchorElNav] = React.useState(null);
  const [anchorElUser, setAnchorElUser] = React.useState(null);
  const history = useHistory();

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };
  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };
  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };


  const showProfile = (event) => {
    event.preventDefault();
    history.push({
      pathname: "/userProfile",
      state: { userId: 3 } //OVDE SE MENJA ID
  })
  };

  const editProfile = (event) => {
    event.preventDefault();
    history.push({
      pathname:"/editUserProfile",
      state: {userId: 3}
    });
  };   


  const showCottageProfile = (event) => {
    event.preventDefault();
    history.push("/showCottageProfile");
  };

  const addCottage = (event) => {
    event.preventDefault();
    history.push("/addCottage");
  };


  const addAdventure = (event) => {
    event.preventDefault();
    history.push("/addAdventure");
  };
  const showAdventureProfile = (event) => {
    event.preventDefault();
    history.push({
      pathname: "/showAdventureProfile",
      state: { adventureId: 203 } //OVDE SE MENJA ID
    })
  };

  const getHomePage = (event) => {
    event.preventDefault();
    history.push("/homePageCottageOwner");
  };   

  return (
    <AppBar position="static" >
      <Container maxWidth="xl" style={{backgroundColor: 'rgb(5, 30, 52)'}}>
        <Toolbar disableGutters>
          <Typography 
            variant="h5"
            noWrap
            component="div"
            sx={{ mr: 2, display: { xs: 'none', color: 'white', fontWeight: "bold", md: 'flex' } }}
            onClick={getHomePage}
          >
            NatureBooking
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: 'flex', color: 'white', fontWeight: "bold",md: 'none' } }}>
            <IconButton 
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu 
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none'},
              }}>
                <MenuItem key={pages[0]} onClick={showCottageProfile}>
                  <Typography textAlign="center">{pages[0]}</Typography>
                </MenuItem>
                <MenuItem key={pages[1]} onClick={addCottage}>
                  <Typography textAlign="center">{pages[1]}</Typography>
                </MenuItem>
                <MenuItem key={pages[2]} onClick={showAdventureProfile}>
                  <Typography textAlign="center">{pages[2]}</Typography>
                </MenuItem>
                <MenuItem key={pages[3]} onClick={addAdventure}>
                  <Typography textAlign="center">{pages[3]}</Typography>
                </MenuItem>

            </Menu>
          </Box>
          <Typography
            variant="h6"
            noWrap
            component="div"
            sx={{ flexGrow: 1, display: { xs: 'flex', color: 'white', fontWeight: "bold", md: 'none' } }}
          >
            NatureBooking
          </Typography>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex'} }}>
              <MenuItem key={pages[0]} onClick={showCottageProfile}>
                  <Typography textAlign="center">{pages[0]}</Typography>
                </MenuItem>
                <MenuItem key={pages[1]} onClick={addCottage}>
                  <Typography textAlign="center">{pages[1]}</Typography>
                </MenuItem>
                <MenuItem key={pages[2]} onClick={showAdventureProfile}>
                  <Typography textAlign="center">{pages[2]}</Typography>
                </MenuItem>
                <MenuItem key={pages[3]} onClick={addAdventure}>
                  <Typography textAlign="center">{pages[3]}</Typography>
                </MenuItem>
          </Box>

          <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="Open settings">
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0}}>
                <Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg" />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
                {
                    <MenuItem key={settings[0]} onClick={showProfile}>
                    <Typography textAlign="center">{settings[0]}</Typography>
                  </MenuItem>
                }
                {
                    <MenuItem key={settings[1]} onClick={editProfile}>
                    <Typography textAlign="center">{settings[1]}</Typography>
                  </MenuItem>
                }
                {
                    <MenuItem key={settings[2]} onClick={handleCloseUserMenu}>
                    <Typography textAlign="center">{settings[2]}</Typography>
                  </MenuItem>
                }
                {
                    <MenuItem key={settings[3]} onClick={handleCloseUserMenu}>
                    <Typography textAlign="center">{settings[3]}</Typography>
                  </MenuItem>
                }
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};
export default Navbar;