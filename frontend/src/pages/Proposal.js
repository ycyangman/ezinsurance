import React, { Component } from 'react';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import DialogContent from '@mui/material/DialogContent';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import InputLabel from '@mui/material/InputLabel';
//import { withStyles } from '@mui/material/styles';
import { withStyles } from '@mui/styles';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import LabelImportantIcon from '@mui/icons-material/LabelImportant';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormLabel from '@mui/material/FormLabel';
import FormControlLabel from '@mui/material/FormControlLabel';

import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

import ApiService from "../common/ApiService";

const style = {
  display: 'flex',
  justifyContent: 'left',
  marginLeft: 30
}

const styles = theme => ({
  root: {
    width: '100%',
    minWidth: 1080,
    marginTop: 10,
    marginLeft: '30px'
  },
  formControl: {
    //margin: theme.spacing(1),
    minWidth: 120,
  },
  selectEmpty: {
    //marginTop: theme.spacing(2),
  },
  table: {
    minWidth: 500,
  },
});


const rows = [
  { prdcd: "P00000001", inscd: "R0001001", insnm: "(무)신보철치료보장특약", insPrd: '', pmPrd: '', pmCyl: '', entAmt: '', prm: '10000' },
  { prdcd: "P00000001", inscd: "R0001002", insnm: "(무)크라운보장특약", insPrd: '', pmPrd: '', pmCyl: '', entAmt: '', prm: '25000' },

  { prdcd: "P00000002", inscd: "R0002001", insnm: "7대고액암진단특약", insPrd: '', pmPrd: '', pmCyl: '', entAmt: '', prm: '20000' },
  { prdcd: "P00000002", inscd: "R0002002", insnm: "유방암진단특약", insPrd: '', pmPrd: '', pmCyl: '', entAmt: '', prm: '30000' },


];

class Proposal extends Component {

  constructor(props) {
    super(props);

    this.state = {
      id: '',
      prdcd: '',
      prdnm: '',
      custNo: '999999999',
      custNm: '',
      aclBirdt: '',
      gndrCd: '',
      insJobCd: '',
      insJobNm: '',
      sprm: '',
      entAmt: '',
      fininCd: '',
      fininNm: '',
      actNo: '', //계좌번호
      achdNm: '', //예금주명
      paymentType: '1',
      ppsdsnNo: '',
      products: [],
      insList: [],
      message: null
    }
  }

  componentDidMount() {

    //const prdcd = location.state?.prdcd


    this.getProducts();



  }

  initState = () => {
    this.setState({
      ppsdsnNo: '',
      prdcd: '',
      prdnm: '',
      custNo: '999999999',
      custNm: '',
      aclBirdt: '',
      gndrCd: '',
      insJobCd: '',
      insJobNm: '',
      sprm: '',
      entAmt: '',
      products: [],
      insList: [],
      message: null
    });

    this.getProducts();
  }

  getProducts = async () => {

    await ApiService.fetchTran('/products')
      .then((response) => {
        console.log(response);
        this.setState({ products: response.data._embedded.products })

        if (this.props.match.params.prdcd) {
          this.setState({ prdcd: this.props.match.params.prdcd })

          this.handleChangeValue("prdcd|prdnm", this.props.match.params.prdcd, null)

        }

      })
      .catch(err => console.log(err));

  }

  addCustomer = async () => {

    let data = {

      svcId: 'CSA001SVC',
      svcFn: 'saveCustomer',

      custNm: this.state.custNm,
      aclBirdt: this.state.aclBirdt,
      gndrCd: this.state.gndrCd,
      insJobCd: this.state.insJobCd,
      insJobNm: this.state.insJobNm,

    }

    console.log(data);

    await ApiService.postTran('/customers/online', data)
      .then((response) => {

        this.setState({ custNo: response.data.data.custNo })
        //alert(response.data.data.custNo);

      })
      .catch(error => {
        console.error('There was an error!', error);
      });


  }

  getInsData = () => {

    if (this.state.prdcd === '') {
      alert('상품을 선택하세요');
      return false;
    }

    let planInfo = {
      ppsdsnNo: this.state.ppsdsnNo,
      ppsdsnDt: "",
      prdcd: this.state.prdcd,
      prdnm: this.state.prdnm,
      custNo: this.state.custNo,
      custNm: this.state.custNm,
      slctPlnrEno: "",
      slctPlnrNm: "",
      slctDofOrgNo: "",
      slctDofOrgNm: "",
      insPrd: "",
      pmPrd: "",
      pmCyl: "",
      sprm: this.state.sprm,
      entAmt: this.state.entAmt,
      progSt: ""
    }

    let insRelsList = [
      {
        ppsdsnNo: "",
        custContRelcd: "11",
        custContRelnm: "계약자",
        custNo: this.state.custNo,
        relpSeq: "",
        custNm: this.state.custNm,
        age: "",
        gndrCd: this.state.gndrCd,
        insJobCd: this.state.insJobCd,
        insJobNm: this.state.insJobNm,
        lastRskGcd: "",
        vhclKcd: "",
        pinsdCustRelcd: "",
        insdCustRelcd: "",
        hobyCd: "",
        rskGcd: "",
        drvgJobCd: ""
      },
      {
        ppsdsnNo: "",
        custContRelcd: "21",
        custContRelnm: "피보험자",
        custNo: this.state.custNo,
        relpSeq: "",
        custNm: this.state.custNm,
        age: "7",
        gndrCd: this.state.gndrCd,
        insJobCd: this.state.insJobCd,
        insJobNm: this.state.insJobNm,
        lastRskGcd: "",
        vhclKcd: "",
        pinsdCustRelcd: "",
        insdCustRelcd: "",
        hobyCd: "",
        rskGcd: "",
        drvgJobCd: ""
      }
    ]

    let data = {

      planInfo: planInfo,
      insRelsList: insRelsList,
      insList: this.state.insList,

    }

    return data;
  }

  //가입
  joinContract = (e) => {
    e.preventDefault();

    const insData = this.getInsData();

    if (!insData) return;

    if (this.state.sprm === '') {

      alert('보험료계산 후 수행하세요');
      return;
    }

    if (this.state.fininCd === '' || this.state.actNo === '' || this.state.achdNm === '') {

      alert('결재정보를 입력하세요');
      return;
    }

    let data = {

      svcId: 'NBA001SVC',
      svcFn: 'saveProposal',

      fininCd: this.state.fininCd, //금융기관코드
      fininNm: this.state.fininNm, //금융기관명
      actNo: this.state.actNo, //계좌번호
      achdNm: this.state.achdNm, //예금주명
      paymentType: this.state.paymentType,

      prpsInfo: insData.planInfo,
      prpsRelsList: insData.insRelsList,
      prpsInsList: insData.insList,

    }

    const config = {
      headers: { 'content-type': 'application/json' }
    }

    ApiService.postTran('/proposals/online', data, config)
      .then((response) => {

        console.log(response.data.data);
        alert('가입되었습니다.');

      })
      .catch(error => {
        console.error('There was an error!', error);
        alert(error);
      });
  }

  //보험료계산 및 저장
  calcPremium = (e) => {
    e.preventDefault();

    //고객등록
    this.addCustomer();

    const { insList } = this.state;

    let sprm = 0;
    let entAmt = 0;
    insList.map((row) => {

      let inscd = row.inscd;
      const insRow = rows.find((c) => {
        return inscd === c.inscd;
      })

      if (insRow) {

        console.log(insRow);
        row.prm = insRow.prm;
        sprm += parseInt(row.prm); //합계보험료

        if (row.entAmt !== '') {
          entAmt += parseInt(row.entAmt); //가입금액
        }

      }

    })

    this.setState({ insList: insList })
    this.setState({ sprm: sprm })
    this.setState({ entAmt: entAmt })

    //window.localStorage.setItem("custNo", custNo);

    const insData = this.getInsData();

    let data = {
      svcId: 'PLA002SVC',
      svcFn: 'savePlan',

      planInfo: insData.planInfo,
      planRelsList: insData.insRelsList,
      planInsList: insData.insList,

    }

    console.log(data);

    const config = {
      headers: { 'content-type': 'application/json' }
    }
    ApiService.postTran('/plans/online', data, config)
      .then((response) => {

        console.log(response.data.data);
        this.setState({ ppsdsnNo: response.data.data.ppsdsnNo });

        alert('저장되었습니다.');

      })
      .catch(error => {
        console.error('There was an error!', error);
        alert(error);
      });

  }

  //상설발행
  prodDesdIsue = () => {

    if (this.state.ppsdsnNo === '') {

      alert('가입설계번호가 없습니다.');
      return;
    }

    let data = {
      svcId: 'PLA003SVC',
      svcFn: 'prodDesdIsue',

      ppsdsnNo: this.state.ppsdsnNo,

    }

    const config = {
      headers: { 'content-type': 'application/json' }
    }

    ApiService.postTran('/plans/online', data, config)
      .then((response) => {

        console.log(response.data.data);
        this.setState({ ppsdsnNo: response.data.data.ppsdsnNo });
        alert('상품설명서 생성 요청 되었습니다.');

      })
      .catch(error => {
        console.error('There was an error!', error);
        alert(error);
      });

  }

  handleChangePrdcd(prdcd) {
    const insList = rows.filter((c) => {

      return c.prdcd.indexOf(prdcd) > -1;
    })

    //deep copy
    const insListCpy = JSON.parse(JSON.stringify(insList))

    //console.log(insListCpy);

    insListCpy.map(row => {
      console.log(row);
      return row.prm = '';
    })

    console.log(insList);

    this.setState({ insList: insListCpy })

  }

  handleValueChange = (e, id) => {

    e.preventDefault();
    let name = e.target.name;
    let value = e.target.value;

    this.handleChangeValue(name, value, id);

  }

  handleChangeValue = (name, value, id) => {

    console.log(id);

    if (id && id.type) {
      this.setState({
        [name.split("|")[0]]: value
      });

      //combo box
      if (name.split("|")[1]) {
        this.setState({
          [name.split("|")[1]]: id.props.children
        });
      }

      //상품코드 변경시
      if (name.indexOf("prdcd") > -1) {

        this.handleChangePrdcd(value)
      }

    }

    else {
      let nextState = {};
      nextState[name] = value;
      this.setState(nextState);
    }

  }

  handleGridValChange = (e) => {

    console.log(e.target.name + '/' + e.target.value);


    let columnId = e.target.name.split("_")[0];
    let value = e.target.value;

    let rowIndex = parseInt(e.target.name.split("_")[1]);
    console.log(columnId + '::' + rowIndex);

    const { insList } = this.state;
    console.log(insList)

    return this.setState({
      // information 배열에서 수정할 id와 같은 id인 경우, 새로운 info 객체를 만들어서 기존의 값과 새로운 data 값을 할당,
      // 다른 경우 기존 값을 그대로 유지
      insList: insList.map((row, index) =>
        index === rowIndex ?
          { ...row, [columnId]: value } : row

      )
    })

  }


  render() {

    const { classes } = this.props;

    const gndrCdList = [
      { cdVl: '1', cdNm: '남자' },
      { cdVl: '2', cdNm: '여자' }
    ];

    const insJobCdList = [
      { cdVl: '000001', cdNm: '무직' },
      { cdVl: '000002', cdNm: '회사원' },
      { cdVl: '000003', cdNm: '공무원' }
    ];


    const insPrdCdList = [
      { cdVl: '10년', cdNm: '10년' },
      { cdVl: '20년', cdNm: '20년' }
    ];

    const pmPrdCdList = [
      { cdVl: '10년', cdNm: '10년' },
      { cdVl: '20년', cdNm: '20년' }
    ];

    const pmCylCdList = [
      { cdVl: '월납', cdNm: '월납' },
      { cdVl: '연납', cdNm: '연납' }
    ];

    const fininCdList = [
      { cdVl: '001', cdNm: '하나은행' },
      { cdVl: '002', cdNm: '우리은행' },
      { cdVl: '003', cdNm: '국민은행' }
    ];

    return (
      <div className={classes.root}>
        <Typography variant="h6" style={style}>보험가입하기</Typography><br />
        <Grid item >
          <LabelImportantIcon color="primary" style={{ verticalAlign: "middle" }} />상품정보
          <DialogContent style={{ padding: '2px' }}>
            <FormControl className={classes.formControl} style={{ width: 150 }}>
              <TextField label=" " type="text" name="prdcd" value={this.state.prdcd} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <InputLabel id="job_label" labelWidth={10}>상품</InputLabel>
              <Select name="prdcd|prdnm"
                id="selPrdcdList"
                style={{ minWidth: 500 }}
                value={this.state.prdcd}
                onChange={this.handleValueChange}
              >
                {
                  this.state.products.map(data => (
                    <option key={data.prdcd} value={data.prdcd}>{data.prdnm}</option>
                  ))
                }
              </Select>
            </FormControl>
          </DialogContent>
        </Grid>
        <Grid item style={{ marginTop: 20 }}>
          <LabelImportantIcon color="primary" style={{ verticalAlign: "middle" }} />고객정보
          <DialogContent style={{ padding: '2px' }}>
            <FormControl className={classes.formControl}>
              <TextField label="이름" type="text" name="custNm" id="custNm" value={this.state.custNm} onChange={this.handleValueChange} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <TextField label="생년월일" type="text" name="aclBirdt" value={this.state.aclBirdt} onChange={this.handleValueChange} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <InputLabel id="gender_label" labelWidth={10}>성별</InputLabel>

              <Select name="gndrCd"
                id={"selGndrCd"}
                style={{ minWidth: 100 }}
                value={this.state.gndrCd}
                onChange={this.handleValueChange}
              >
                {
                  gndrCdList && gndrCdList.map(data => (
                    <MenuItem key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem>
                  ))
                }
              </Select>
            </FormControl>
            <FormControl className={classes.formControl}>
              <InputLabel id="job_label" labelWidth={10}>직업</InputLabel>
              <Select name="insJobCd|insJobNm"
                id="selInsJobCdList"
                style={{ minWidth: 100 }}
                value={this.state.insJobCd}
                onChange={this.handleValueChange}
              >
                {
                  insJobCdList && insJobCdList.map(data => (
                    <MenuItem key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem>
                  ))
                }
              </Select>
            </FormControl>
          </DialogContent>
        </Grid>
        <Grid item style={{ marginTop: 20, marginBottom: 20 }}>
          <LabelImportantIcon color="primary" style={{ verticalAlign: "middle" }} />보험정보
        </Grid>

        <TableContainer style={{ width: '900px', border: '1px solid' }} component={Paper}>
          <Table className={classes.table} aria-label="spanning table">
            <TableHead>
              <TableRow>
                <TableCell align="center">보험명</TableCell>
                <TableCell align="center">보험기간</TableCell>
                <TableCell align="center">납입기간</TableCell>
                <TableCell align="center">납입주기</TableCell>
                <TableCell align="center">가입금액</TableCell>
                <TableCell align="center">보험료</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {

                this.state.insList.map((row, index) => (


                  <TableRow key={row.desc}>
                    <TableCell align="left">{row.insnm}</TableCell>
                    <TableCell align="center">
                      <Select name={`insPrd_${index}`}
                        id="selInsPrd"
                        style={{ minWidth: 100 }}
                        onChange={(e) => this.handleGridValChange(e)}
                      >
                        {
                          insPrdCdList && insPrdCdList.map(data => (
                            <MenuItem key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem>
                          ))
                        }
                      </Select>
                    </TableCell>
                    <TableCell align="center">
                      <Select key={index} name={`pmPrd_${index}`}
                        id="selPmPrd"
                        style={{ minWidth: 100 }}
                        onChange={this.handleGridValChange}
                      >
                        <MenuItem></MenuItem>
                        {
                          pmPrdCdList && pmPrdCdList.map(data => (
                            <MenuItem key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem>
                          ))
                        }
                      </Select>

                    </TableCell>
                    <TableCell align="center">
                      <Select name={`pmCyl_${index}`}
                        id="selPmCyl"
                        style={{ minWidth: 100 }}
                        onChange={this.handleGridValChange}
                      >
                        {
                          pmCylCdList && pmCylCdList.map(data => (
                            <MenuItem key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem>
                          ))
                        }

                      </Select>
                    </TableCell>
                    <TableCell align="center">
                      <TextField label="" type="text" style={{ width: 100 }} name={`entAmt_${index}`} onChange={this.handleGridValChange} />
                    </TableCell>
                    <TableCell align="right">
                      <TextField label="" type="text" style={{ width: 100 }} name={`prm_${index}`} value={row.prm} onChange={this.handleGridValChange} />
                    </TableCell>
                  </TableRow>

                ))}

              <TableRow>
                <TableCell colSpan={4}> </TableCell>
                <TableCell align="right">{this.state.entAmt} </TableCell>
                <TableCell align="right">
                  <TextField label="" type="text" style={{ width: 100 }} name="sprm" value={this.state.sprm} onChange={this.handleValueChange} />
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>

        <Grid item style={{ marginTop: 20 }}>
          <LabelImportantIcon color="primary" style={{ verticalAlign: "middle" }} />결재정보
          <DialogContent style={{ padding: '2px' }}>
            <FormControl className={classes.formControl}>
              <InputLabel id="finin_label" labelWidth={10}>금융기관</InputLabel>

              <Select name="fininCd|fininNm"
                id={"selFininCd"}
                style={{ minWidth: 150 }}
                value={this.state.fininCd}
                onChange={this.handleValueChange}
              >
                {
                  fininCdList && fininCdList.map(data => (
                    <MenuItem key={data.cdVl} value={data.cdVl}>{data.cdNm}</MenuItem>
                  ))
                }
              </Select>
            </FormControl>
            <FormControl className={classes.formControl}>
              <TextField label="예금주" type="text" name="achdNm" id="achdNm" value={this.state.achdNm} onChange={this.handleValueChange} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <TextField label="계좌번호" type="text" name="actNo" value={this.state.actNo} onChange={this.handleValueChange} />
            </FormControl>
            <FormControl className={classes.formControl}>
              <FormLabel component="legend"> 전송방식 </FormLabel>
              <RadioGroup row name="paymentType" value={this.state.paymentType} onChange={this.handleValueChange} defaultValue={this.state.paymentType}>
                <FormControlLabel value="1" control={<Radio />} label="sync" />
                <FormControlLabel value="2" control={<Radio />} label="async" />
              </RadioGroup>
            </FormControl>
          </DialogContent>
        </Grid>

        <Box style={{ width: '900px', textAlign: "right", marginTop: "20px", marginBottom: "5px" }}>
          <Button variant="contained" color="primary" style={{ height: 30, marginRight: 5 }} onClick={this.initState}>초기화</Button>
          <Button variant="contained" color="primary" style={{ height: 30, marginRight: 5 }} onClick={this.calcPremium}>보험료계산</Button>
          <Button variant="contained" color="primary" style={{ height: 30, marginRight: 5 }} onClick={this.prodDesdIsue}>상품설명서</Button>
          <Button variant="contained" color="primary" style={{ height: 30, marginRight: 5 }} onClick={this.joinContract}>가입</Button>
        </Box>
      </div>
    );
  }
}


export default withStyles(styles)(Proposal);