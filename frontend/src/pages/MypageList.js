import React, { useState, useEffect, useCallback } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import FormControl from '@mui/material/FormControl';
import LabelImportantIcon from '@mui/icons-material/LabelImportant';
import Typography from '@mui/material/Typography'
import { DataGrid, GridToolbar } from '@mui/x-data-grid';

import ApiService from "../common/ApiService";

    const columns = [
      { field: 'custNo', headerName: '고객번호', headerAlign: 'center', align:'center', width: 150 },
      {
        field: 'custNm',
        headerName: '고객명',
        headerAlign: 'center',
        align:'center',
        width: 120,
        editable: false,
      },
      {
        field: 'prdnm',
        headerName: '상품',
        headerAlign: 'center',
        width: 300,
        editable: false,
      },
      {
        field: 'ppsdsnNo',
        headerName: '설계번호',
        headerAlign: 'center',
        align:'center',
        width: 150,
        editable: false,
      },
      {
        field: 'ppsdsnDt',
        headerName: '설계일자',
        headerAlign: 'center',
        align:'center',
        width: 150,
        editable: false,
      },
      {
        field: 'prodDesdIsueDt',
        headerName: '상설발행일자',
        headerAlign: 'center',
        align:'center',
        width: 200,
        editable: false,
      },
      {
        field: 'prpsNo',
        headerName: '청약번호',
        headerAlign: 'center',
        align:'center',
        width: 200,
        editable: false,
      },
      {
        field: 'prpsDt',
        headerName: '청약일자',
        headerAlign: 'center',
        align:'center',
        width: 150,
        editable: false,
      }, 
      {
        field: 'prpsStnm',
        headerName: '청약상태',
        headerAlign: 'center',
        align:'center',
        width: 150,
        editable: false,
      }, 
      {
        field: 'eventType',
        headerName: 'eventType',
        headerAlign: 'center',
        align:'center',
        width: 150,
        editable: false,
      }, 
      {
        field: 'regDtm',
        headerName: '등록일시',
        headerAlign: 'center',
        align:'center',
        width: 150,
        editable: false,
      },  
    ];



   /*
    상태관리는 useState 훅으로 라이프사이클메서드는 useEffect 훅을 사용
    펑션 컴포넌트로만 구성한다면 반드시 사용해야 하는 훅
    펑션 컴포넌트는 상태가 바뀌거나 props 가 변하면 렌더링이 발생
   */

    export default function MypageList() {
      
      const [loading, setLoading] = useState(false)
      const [rows, setRows] = useState([])
      const [error, setError] = useState(false)
      const [query, setQuery] = useState('1');
      const [param, setParam] = useState({
        custNm: "",
        custNo: ""
    })


      const getMsgs = async () => {
        try {
            const result = await ApiService.postTran('/mypages/getMypages', param)
            return result.data.data
        } catch(err) {
            throw err
        }
        
      }

      //useCallback => useMemo와 비슷한 함수
      const loadData = async () => {

        setRows([]);

        try {
            setLoading(true)
            await getMsgs() // getMsgs가 끝날때 까지 기다림
            .then(response => {

              console.log(response)
              response && setRows(response)
            })

        }
         catch (err) {
            setError(err)
            console.error("res msgs Error", err)
        }
         finally {
            setLoading(false) // 스피너 false
        }
    }; //setError가 변경될때만 함수 재생성

    // 어떠한 state가 변화되더라도 컴포넌트를 re-render하지 않음[]
      useEffect(() => {

        loadData();

      }, [query]);

     const goSearch = (rand) => {

      console.log(param)

        setQuery(rand)
      }


      return (
        <div style={{ height: 500, width: '90%',marginLeft: "5px"}}>
          <Typography variant="h6" ><LabelImportantIcon color="primary" style={{verticalAlign:"middle"}}/>MyIns List</Typography><br/>
          <Box style={{textAlign: "right", marginBottom: "5px"}}>

          <FormControl>
          <TextField label="성명" type="text" name="custNm" id="custNm" value={param.custNm} onChange={(event) => setParam(param =>({...param, custNm:event.target.value}) )}/> 
          <input type="hidden" value={query} onChange={(event) => setQuery(event.target.value)} />
          </FormControl> 
            <Button variant="contained" color="primary" style={{ height: 30}} onClick={() => goSearch(parseInt(query)+1)}>조회</Button>
          </Box>
          <DataGrid
            getRowId={(r) => r.id}  
            rowHeight={45}
            rows={ rows }
            columns={columns}
            pageSize={20}
            components={{
              Toolbar: GridToolbar,
            }}
            disableSelectionOnClick
            
          />
        </div>
      );

  }

