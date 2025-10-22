import React, { Component } from 'react';
import CustomerAdd from '../components/CustomerAdd';
import CustomerDelete from '../components/CustomerDelete';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import Typography from '@mui/material/Typography'
import { withStyles } from '@mui/styles';
import { alpha } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import LabelImportantIcon from '@mui/icons-material/LabelImportant';

import ApiService from "../common/ApiService";

const style = {
  display: 'flex',
  justifyContent: 'left',
  marginLeft:30
}

const styles = ({theme}) => ({
  root: {
    width: '90%',
    minWidth: 1080,
	  marginTop: 10
  },
  formControl: {
    //margin: theme.spacing(0),
    minWidth: 250,
  },
  menu: {
    marginTop: 15,
    marginBottom: 15,
    height:20,
    display: 'flex',
    justifyContent: 'flex-end'
  },
  paper: {
    marginLeft: 18,
    marginRight: 0
  },
  progress: {
    //margin: theme.spacing.unit * 2
  },
  grow: {
    flexGrow: 1,
  },
  tableHead: {
    fontSize: '1.0rem',
	backgroundColor: '#a8cfed',
	padding: '10px',
  },
  tableRow: {
    padding: '1px',
	textAlign : 'center'
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
//    '&:hover': {
//      backgroundColor: alpha(theme.palette.common.white, 0.25),
//    },
    marginLeft: 0,
    width: '100%',
//    [theme.breakpoints.up('sm')]: {
      //marginLeft: theme.spacing.unit,
//      width: 'auto',
//    },
  },
  searchIcon: {
//    width: theme.spacing.unit * 9,
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
 //   },
  }
});

class CustomreList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      customers: [],
      completed: 0,
      searchKeyword: ''
    }
  }

  stateRefresh = () => {
    this.setState({
      customers: [],
      completed: 0,
      searchKeyword: ''
    });

    this.search();
  }

  search = () => {
    this.setState({ customers: [] });

    this.callApi()
      .then((res) => {
        this.setState({customers: res})
      })
      .catch(err => console.log(err));
  }

  componentDidMount() {
    this.timer = setInterval(this.progress, 20);

    this.search();
  }

  callApi = async () => {
  
    const response = await ApiService.fetchTran('/customers')

    console.log(response);

    const body = response.data._embedded.customers;
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

  getCodeNm = (cdList, cdVl) => {

    const code = cdList.find((cd) => {
     return cdVl === cd.cdVl 
    })
        
    if(code) {
      return code.cdNm;
    }
    return ''

  }

  render() {
  
  const { classes } = this.props;
  const gndrCdList = [
    {cdVl:'1',cdNm:'남자'},
    {cdVl:'2',cdNm:'여자'}
  ]
	const cellList = ["번호", "성명", "생년월일", "성별", "직업", "설정"];
    
	return (
      <div className={classes.root}>
	    <Typography variant="h6" style={style}><LabelImportantIcon color="primary" style={{verticalAlign:"middle"}}/>Customer List</Typography><br/>
      <Box style={{textAlign: "right", marginBottom: "5px"}}>
        <Button variant="contained" color="primary" style={{ height: 30}} onClick={this.search}>조회</Button>
      </Box>
        <Paper className={classes.paper}>
          <Table className={classes.table}>
            <TableHead>
              <TableRow>
                {cellList.map(c => 
                 <TableCell align="center" className={classes.tableHead}>{c}</TableCell>
                )}
              </TableRow>
            </TableHead>
            
            <TableBody className={classes.tableBody}>
              { 
                //customers ? 
                this.state.customers.map((c) => {
                  
                  var tmpArr = c._links.customer.href.split("/");
                  var customerId = tmpArr[tmpArr.length-1];
               
                  return (
                    <TableRow key={c.custNo} className={classes.tableRow}>
                            <TableCell className={classes.tableRow}>{c.custNo}</TableCell>
                            <TableCell className={classes.tableRow}>{c.custNm}</TableCell>
                            <TableCell className={classes.tableRow}>{c.aclBirdt}</TableCell>
                            <TableCell className={classes.tableRow}>
                              { this.getCodeNm(gndrCdList, c.gndrCd )
                             }
                            </TableCell>
                            <TableCell className={classes.tableRow}>{c.insJobNm}</TableCell>
                            <TableCell className={classes.tableRow}><CustomerDelete stateRefresh={this.stateRefresh} id={customerId}/></TableCell>
                        </TableRow>
                 )

                })
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

export default withStyles(styles)(CustomreList);
