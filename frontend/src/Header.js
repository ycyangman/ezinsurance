import React from "react";
import PropTypes from 'prop-types';
import AppBar from '@mui/material/AppBar';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
//import Hidden from '@mui/material/Hidden';
import IconButton from '@mui/material/IconButton';
//import Link from '@mui/material/Link';
//import MenuIcon from '@material-ui/icons/Menu';
//import NotificationsIcon from '@material-ui/icons/Notifications';
import AlarmIcon from '@mui/icons-material/Alarm';
//import CircleNotificationsIcon from '@mui/icons-material/CircleNotifications';

import Tab from '@mui/material/Tab';
import Tabs from '@mui/material/Tabs';
import Toolbar from '@mui/material/Toolbar';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import { withStyles } from '@mui/styles';
import { Link, Route, BrowserRouter, Routes } from "react-router-dom";

import Proposal from "./pages/Proposal";
import Intro from "./pages/Intro";
import CustomerList from "./pages/CustomerList";
import ProductList from "./pages/ProductList";
import PlanList from "./pages/PlanList";
import AlarmList from "./pages/AlarmList";
import MypageList from "./pages/MypageList";

const lightColor = 'rgba(255, 255, 255, 0.7)';

const styles = (theme) => ({
  secondaryBar: {
    zIndex: 0,
  },
  menuButton: {
    //marginLeft: -theme.spacing(1),
  },
  iconButtonAvatar: {
    padding: 4,
  },
  link: {
    textDecoration: 'none',
    color: lightColor,
    '&:hover': {
      //color: theme.palette.common.white,
    },
  },
  button: {
    borderColor: lightColor,
  },
});



//function Header(props) {
class  Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
        toggle: false,
        value: ''
        };
        }

        static propTypes = {
            children: PropTypes.object.isRequired,
          };

        handleDrawerToggle = () => this.setState({toggle: !this.state.toggle})
        onDrawerToggle = () => this.setState({toggle: !this.state.toggle})


        handleValueChange = (e, newValue) => {

            this.setState({
                value: newValue
            })
        }
        
        
    render() {

  const { classes} = this.props;

  return (
    <React.Fragment>
      <div>
        <BrowserRouter>
        <AppBar color="primary" position="sticky" elevation={0}>
            <Toolbar>
            <Grid item>
              <IconButton color="inherit" className={classes.iconButtonAvatar}>
                <Avatar src="logo.png" alt="React" />
              </IconButton>
            </Grid>
              <Typography color="inherit" variant="h5">
                <Link className={classes.link} to="/" >
                EZINS
                </Link>
              </Typography>
          <Grid container spacing={1} alignItems="center">
		  {/*
            <Hidden smUp>
              <Grid item>
                <IconButton
                  color="inherit"
                  aria-label="open drawer"
                  className={classes.menuButton}
                >
                  <MenuIcon />
                </IconButton>
              </Grid>
            </Hidden>
		  */}
            <Grid item xs />
            <Grid item>
              <Link className={classes.link} href="#" variant="body2" onClick={() => window.open('https://github.com/sklinaproject/project', '_blank')}>
                Go to download
              </Link>
            </Grid>
            <Grid item>
              <Tooltip title="Alerts • No alerts">
                <IconButton color="inherit">
                  <AlarmIcon />
                </IconButton>
              </Tooltip>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>

     
      <AppBar
        component="div"
        className={classes.secondaryBar}
        color="primary"
        position="static"
        elevation={0}
      >
        <Tabs value={this.state.value} textColor="inherit" indicatorColor="primary"  onChange={this.handleValueChange}>
          <Tab textColor="inherit" label="Product" component={Link} to="/ProductList" />
		  <Tab textColor="inherit" label="Plan" component={Link} to="/PlanList" />
          <Tab textColor="inherit" label="Customer" component={Link} to="/CustomerList" />
          <Tab textColor="inherit" label="Join" component={Link} to="/Joins"  />
          <Tab textColor="inherit" label="Mypage" component={Link} to="/MypageList" />
          <Tab textColor="inherit" label="Alarm" component={Link} to="/AlarmList"  />
        </Tabs>
      </AppBar>
	  <AppBar color="primary" position="sticky" elevation={0}>
	  </AppBar>
	  <Routes>
      <Route exact path="/" element={<Intro />} />
	  </Routes>
      <Routes>
            <Route path="/ProductList" element={<ProductList />} />
			<Route path="/PlanList" element={<PlanList />} />
            <Route path="/CustomerList" element={<CustomerList />} />
            <Route path="/Joins" element={<Proposal />} />
            <Route path="/MypageList" element={<MypageList />} />
            <Route path="/AlarmList" element={<AlarmList />} />
      </Routes>
      </BrowserRouter>
      </div>

      <div id="content" style={{margin: 'auto', marginTop: '20px'}}>
      </div>

    </React.Fragment>
  );
}
}

Header.propTypes = {
  classes: PropTypes.object.isRequired,
  onDrawerToggle: PropTypes.func.isRequired,
};

export default withStyles(styles)(Header);
