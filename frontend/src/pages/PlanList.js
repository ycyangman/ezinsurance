import React, { Component } from 'react';
import CustomerAdd from '../components/CustomerAdd';
import CustomerDelete from '../components/CustomerDelete';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import CircularProgress from '@mui/material/CircularProgress';
import { withStyles } from '@mui/styles';
import { alpha } from '@mui/material/styles';
//import SearchIcon from '@material-ui/icons/Search';

import ApiService from "../common/ApiService";

const styles = theme => ({
  root: {
    width: '100%',
    minWidth: 1080
  },
  menu: {
    marginTop: 15,
    marginBottom: 15,
    display: 'flex',
    justifyContent: 'center'
  },
  paper: {
    marginLeft: 18,
    marginRight: 18
  },
  progress: {
//    margin: theme.spacing.unit * 2
  },
  grow: {
    flexGrow: 1,
  },
  tableHead: {
    fontSize: '1.0rem'
  },
  menuButton: {
    marginLeft: -12,
    marginRight: 20,
  },
  title: {
    display: 'none',
//    [theme.breakpoints.up('sm')]: {
//      display: 'block',
//    },
  },
  search: {
    position: 'relative',
//    borderRadius: theme.shape.borderRadius,
//    backgroundColor: alpha(theme.palette.common.white, 0.15),
 //   '&:hover': {
//      backgroundColor: alpha(theme.palette.common.white, 0.25),
//    },
    marginLeft: 0,
    width: '100%',
//    [theme.breakpoints.up('sm')]: {
//      marginLeft: theme.spacing.unit,
//      width: 'auto',
//    },
  },
  searchIcon: {
    //width: theme.spacing.unit * 9,
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  inputRoot: {
    color: 'inherit',
    width: '100%',
  },
  inputInput: {
    //paddingTop: theme.spacing.unit,
    //paddingRight: theme.spacing.unit,
    //paddingBottom: theme.spacing.unit,
    //paddingLeft: theme.spacing.unit * 10,
    //transition: theme.transitions.create('width'),
    width: '100%',
//    [theme.breakpoints.up('sm')]: {
//      width: 120,
//      '&:focus': {
//        width: 200,
//      },
//    },
  }
});

class PlanList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      plans: '',
      completed: 0,
      searchKeyword: ''
    }
  }

  stateRefresh = () => {
    this.setState({
      plans: '',
      completed: 0,
      searchKeyword: ''
    });
    this.callApi()
      .then(res => this.setState({plans: res}))
      .catch(err => console.log(err));
  }

  componentDidMount() {
    this.timer = setInterval(this.progress, 20);
    this.callApi()
      .then(res => this.setState({plans: res}))
      .catch(err => console.log(err));
  }

  callApi = async () => {
    
    const response = await ApiService.fetchTran('/plans');    
    console.log(response);

    const body = response.data._embedded.plans;
    console.log("body:"+body);
    return body;
  }

  progress = () => {
    const { completed } = this.state;
    this.setState({ completed: completed >= 100 ? 0 : completed + 1});
  }

  handleValueChange = (e) => {
    let nextState = {};
    nextState[e.target.name] = e.target.value;
    this.setState(nextState);
  }

  render() {
    
	const filteredComponents = (data) => {
      data = data.filter((c) => {
        return c.custNm.indexOf(this.state.searchKeyword) > -1;
      });
      return data.map((c) => {
        return (
		    <TableRow>
                <TableCell>{c.ppsdsnNo}</TableCell>
                <TableCell>{c.ppsdsnDt}</TableCell>
                <TableCell>{c.prdnm}</TableCell>
                <TableCell>{c.custNm}</TableCell>
                <TableCell>{c.sprm}</TableCell>
                <TableCell>{c.progSt}</TableCell>
            </TableRow>
		
		)
      });
    }
    
	const { classes } = this.props;
    
	const cellList = ["가입설계번호", "설계일자", "상품명", "고객명", "합계보험료", "진행상태"];
    
	return (
      <div className={classes.root}>
        <Paper className={classes.paper}>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
                {cellList.map(c => {
                  return <TableCell className={classes.tableHead}>{c}</TableCell>
                })}
              </TableRow>
            </TableHead>
            <TableBody>
              {this.state.plans ? 
                filteredComponents(this.state.plans) :
              <TableRow>
                <TableCell colSpan="6" align="center">
                  <CircularProgress className={classes.progress} variant="determinate" value={this.state.completed}/>
                </TableCell>
              </TableRow>
              }
            </TableBody>
          </Table>
        </Paper>
        <div className={classes.menu}>
          <CustomerAdd stateRefresh={this.stateRefresh}/>
        </div>
      </div>
    );
  }
}

export default withStyles(styles)(PlanList);
