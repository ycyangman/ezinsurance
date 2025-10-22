
import React, { Component } from 'react';
//import { withStyles } from '@mui/material/styles';
import LabelImportantIcon from '@mui/icons-material/LabelImportant';
import Typography from '@mui/material/Typography'
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import FormControl from '@mui/material/FormControl';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';

import ApiService from "../common/ApiService";

class AlarmList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      custNm:'',
      mypages: [],
      completed: 0,
      searchKeyword: ''
    }
  }

  componentDidMount() {
    this.search();
  }

  search = () => {

    let param = {
      custNm : this.state.custNm,
    }

    this.setState({ mypages: []});

    this.callApi(param)
      .then(res => this.setState({mypages: res}))
      .catch(err => console.log(err));
  }

  callApi = async (param) => {
    
    const response = await ApiService.postTran('/msgs/getMsgs', param)

    console.log(response);

    const body = response.data.data;
    console.log("body:"+body);
    return body;
  }

  handleValueChange = (e) => {

    let nextState = {};
    nextState[e.target.name] = e.target.value;
    this.setState(nextState);

}

  render() {

    const columns = [
      { field: 'custNo', headerName: '고객번호', headerAlign: 'center', align:'center', width: 150 },
      {
        field: 'custNm',
        headerName: '고객명',
        headerAlign: 'center',
        align:'center',
        width: 200,
        editable: false,
      },
      {
        field: 'message',
        headerName: '전달내용',
        headerAlign: 'center',
        width: 400,
        editable: false,
      },
      {
        field: 'sendDtm',
        headerName: '전송일시',
        headerAlign: 'center',
        align:'center',
        width: 250,
        editable: false,
      },
    ];

    return (
      <div style={{ marginTop: "5px", marginLeft: "10px", height: 400, width: '92%', alignItems: "center" }}>
      
      <Typography variant="h6" ><LabelImportantIcon color="primary" style={{verticalAlign:"middle"}}/>Alarm List</Typography><br/>
      <Box style={{textAlign: "right", marginBottom: "5px"}}>
        <FormControl>
          <TextField label="성명" type="text" name="custNm" id="custNm" value={this.state.custNm} onChange={this.handleValueChange}/> 
        </FormControl> 
      <Button variant="contained" color="primary" style={{ height: 30}} onClick={this.search}>조회</Button>
      </Box>

      <div style={{ height: 400 } }>
      <DataGrid
            getRowId={(r) => r.id}  
            rowHeight={45}
            rows={ this.state.mypages }
            columns={columns}
            pageSize={20}
            components={{
              Toolbar: GridToolbar,
            }}
            disableSelectionOnClick
            
          />
      </div>
      </div>
    );

  }
}

export default (AlarmList);