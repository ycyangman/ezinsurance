## ez보험가입

본 프로젝트는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하고 보험가입서비스 쉽게 따라하기 입니다.

- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW


# Table of contents

  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [DDD 의 적용](#ddd-의-적용)
    - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
    - [폴리글랏 프로그래밍](#폴리글랏-프로그래밍)
    - [동기식 호출 과 Fallback 처리](#동기식-호출-과-Fallback-처리)
    - [비동기식 호출 과 Eventual Consistency](#비동기식-호출-과-Eventual-Consistency)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출-서킷-브레이킹-장애격리)
    - [오토스케일 아웃](#오토스케일-아웃)
    - [무정지 재배포](#무정지-재배포)
  - [신규 개발 조직의 추가](#신규-개발-조직의-추가)

# 서비스 시나리오

배달의 민족 커버하기 - https://1sung.tistory.com/106

기능적 요구사항
1. 보험상품관리자는 상품을 등록하고 변경한다.
1. 고객이 고객정보 등록을 한다.
1. 고객이 상품을 선택하여 보험료를 계산한다.
1. 보험료 산출시 보험료 인수조건(한도금액, 가입연령등)을 체크한다. 
3. 중간중간 고객은 가입설계 내역을 저장한다.
4. 고객은 보험료 계산후 상품설명서 발행을 요청한다.
5. 상품설명서가 발행되면 알림톡으로 발행내역을 전달한다
6. 고객은 상품내역을 확인하고 청약을 진행한다.
7. 고객이 보험의 초회보험료를 결제한다.
8. 고객은 청약을 철회요청할 수 있다.
9. 청약철회되면 결제가 취소 된다.
10. 고객이나 보험관리자는 보험가입상태를 중간중간 조회한다.

비기능적 요구사항
1. 트랜잭션
    1. 결제가 되지 않은 청약건은 계약이 성립되지 않아야 한다 Sync 호출
1. 장애격리
    1. 청약관리 기능이 수행되지 않더라도 가입설계시스템은 24/365 시스템으로 운영 되어야 한다  Async (event-driven), Eventual Consistency
    1. 결제시스템이 과중되면 사용자를 잠시동안 받지 않고 결제를 잠시후에 하도록 유도한다  Circuit breaker, fallback
1. 성능
    1. 고객과 보험관리자는 청약상태를 해당 시스템에서 확인할 수 있다 CQRS
    1. 가 바뀔때마다 카톡 등으로 알림을 줄 수 있어야 한다  Event driven


# 체크포인트

- 분석 설계


  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계


## Event Storming 결과

### 이벤트 도출
 우선 시간의 흐름에 따라 비지니스의 상태 변경(생성,변경,삭제 등)을 의미하는 도메인 이벤트를 도출한다
![이벤트도출](https://user-images.githubusercontent.com/84304227/124475013-3106b980-dddc-11eb-935f-6e2b15f5f102.PNG)

### 부적격 이벤트 탈락
![이벤트탈락](https://user-images.githubusercontent.com/84304227/124475020-349a4080-dddc-11eb-9154-33e47ce4657e.PNG)

    - 과정중 도출된 잘못된 도메인 이벤트들을 걸러내는 작업을 수행함
        -  UI 의 이벤트, 업무적인 의미의 이벤트가 아니라서 제외

### 액터, 커맨드 부착하여 읽기 좋게
- 엑터는 사람이나 조직이 될 수 있는데 역할 관점으로 도출한다. 엑터는 추상적으로 식별하지 말고 비지니스를 수행하는 구체적인 역할로 고려하여 도출한다. 
- 이벤트를 트리거하는(발생시키는) 커맨드를 도출한다. 커맨드는 동사 형태가 된다.

### 어그리게잇으로 묶기
어그리게잇은 커맨드와 이벤트가 영향을 주는 데이터 요소이다.
명사형이고 노란색 포스트잇에 작성하여 커맨드와 이벤트 사이의 상단에 겹쳐서 붙인다.

### 바운디드 컨텍스트로 묶기

 - 도메인 서열 분리 
   - Core Domain:  app(front), store : 없어서는 안될 핵심 서비스이며, 연견 Up-time SLA 수준을 99.999% 목표, 배포주기는 app 의 경우 1주일 1회 미만, store 의 경우 1개월 1회 미만
  - Supporting Domain:   marketing, customer : 경쟁력을 내기위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 각 팀의 자율이나 표준 스프린트 주기가 1주일 이므로 1주일 1회 이상을 기준으로 함.
  - General Domain:   pay : 결제서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음 (핑크색으로 이후 전환할 예정)
- 
### 폴리시 부착 
정책은 이벤트 뒤에 따라오는 반응 적인 비지니스 로직이며 어디인가에 존재하는 커맨드를 트리거 한다.

![폴리시부착](https://user-images.githubusercontent.com/84304227/124947288-a972b200-e04a-11eb-9afb-f216e96bd923.PNG)

### 최종 모델링

![ezInsurance_event](https://user-images.githubusercontent.com/84304227/124379147-3ab2f300-dcf0-11eb-8c88-5a363cf2e5b5.PNG)

## 헥사고날 아키텍처 다이어그램 도출
    
![헥사고날](https://user-images.githubusercontent.com/84304227/124379158-4c949600-dcf0-11eb-94d6-ef8fe1363377.png)

    - Chris Richardson, MSA Patterns 참고하여 Inbound adaptor와 Outbound adaptor를 구분함
    - 호출관계에서 PubSub 과 Req/Resp 를 구분함
    - 서브 도메인과 바운디드 컨텍스트의 분리:  각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐

## 이벤트스토밍 구현 기술연동

![이벤트스토밍구현기술연동](https://user-images.githubusercontent.com/84304227/122505402-3ec7eb00-d037-11eb-9d12-f03875dd68ad.PNG)

# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현하였다. 
구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

## CQRS
CQRS는 Command and Query Responsibility Segregation(명령과 조회의 책임 분리)을 나타냅니다.

가입설계저장/상품설명서발행/청약저장/결재(초회입급)/ 등이 진행상태 에 대하여 관리자와 고객이 조회 할 수 있도록 CQRS 로 구현하였다.

```
@Service
public class MypageViewHandler {


    @Autowired
    private MypageRepository mypageRepository;

    //-----------------------------------------------------
    // 이벤트 수신
    //-----------------------------------------------------
    @StreamListener(KafkaProcessor.INPUT)
    public void whenEventOccurred (@Payload Mypage eventInfo) {
        try {

            //가입설계
            if( "PlanSaved".equalsIgnoreCase(eventType) || "PrductDesdCreated".equalsIgnoreCase(eventType)) {
                
                List<Mypage> mypageList = mypageRepository.findByCustNoAndPpsdsnNo(custNo, ppsdsnNo);

                if(ObjectUtils.isEmpty(mypageList)) {
                    Mypage mypage = new Mypage();
                    BeanUtils.copyProperties(eventInfo, mypage);
    
                    mypageRepository.save(mypage);
                }
    
                for(Mypage mypage : mypageList){
    
                    String progSt = eventInfo.getProgSt();
                    if( "PrductDesdCreated".equalsIgnoreCase(eventType)) {
                        
                        if(StringUtils.isEmpty(progSt)) {
                            progSt = "상품설명서생성완료";
                        }

                        mypage.setProdDesdIsueDt(DateUtils.getCurrentDate(DateUtils.EMPTY_DATE_TYPE));
                        
                    }

                    mypage.setEventType(eventType);
                    mypage.setProgSt(progSt);

                    mypage.setChgDtm(DateUtils.getCurDtm());

                    mypageRepository.save(mypage);
                }
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
```
![mypage_가설](https://user-images.githubusercontent.com/84304227/124385658-a8bbe200-dd11-11eb-9818-718e8d05eeb7.PNG)

![mypage_상설](https://user-images.githubusercontent.com/84304227/124385660-ad809600-dd11-11eb-86dc-ebcba9c3a119.PNG)

![mypage_청약](https://user-images.githubusercontent.com/84304227/124385812-4b746080-dd12-11eb-8186-7fdfcb253e40.PNG)

## API 게이트웨이
1. gateway 스프링부트 App을 추가 후 application.yaml내에 각 마이크로 서비스의 routes 를 추가하고 gateway 서버의 포트를 8080 으로 설정함
- application.yaml 예시

```
spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: plan
          uri: http://plan:8080
          predicates:
            - Path=/plans/** 
        - id: customer
          uri: http://customer:8080
          predicates:
            - Path=/customers/** 
        - id: payment
          uri: http://payment:8080
          predicates:
            - Path=/payments/** 
        - id: alarm
          uri: http://alarm:8080
          predicates:
            - Path=/msgs/** 
        - id: myinsurance
          uri: http://myinsurance:8080
          predicates:
            - Path= /mypages/**
        - id: proposal
          uri: http://proposal:8080
          predicates:
            - Path=/proposals/** 
        - id: product
          uri: http://product:8080
          predicates:
            - Path=/products/**, /productDocuments/** 
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080
```

## Correlation(코리아그패피 SAGA패턴)
프로젝트에서는 마이크로 서비스간 트랜잭션 처리시 서비스간 데이터 일관성을 유지하기 위해 업무간 Correlation-key 를 이벤트로 전달받아 
서비스간 연관된 처리를 순차적으로 구현한다.

각각의 MSA 서비스는 자신이 보유한 서비스내 Local 트랜잭션을 관리하며, 트랜잭션이 종료되면 완료 Event를 발행합니다. 
만약 그 다음에 수행되어야할 트랜잭션이 있다면,  해당 트랜잭션을 수행해야하는 App에서 완료 Event를 수신받고 다음 작업을 처리한다.
이때 Event는 Kafka와 같은 메시지 브로커를 이용해서 메시지기반의 비동기 방식으로 전달한다.

가입설계를 하면 동시에 상품설계서발행(Prodcut)이 요청되며 발행이되면 서비스의 상태가 발행완료 되고
청약시 결재(초회입금)을 취소하면 다시 연관된 ,청약 등의 서비스의 상태값 등의 데이터가 변경되는 것을 확인할 수 있습니다.

```
가입설계조회
http --json POST localhost:8080/plans/online svcId="PLA002SVC" svcFn="getPlan" ppsdsnNo="20210630180956" custNo="000000001"
```
![가입설계조회](https://user-images.githubusercontent.com/84304227/124379858-6fc14480-dcf4-11eb-8e64-2444821c8731.PNG)
```
상품설명서 발행요청(10초지연)
http --json POST localhost:8080/plans/online svcId="PLA003SVC" svcFn="prodDesdIsue" ppsdsnNo="20210630180956"
```
![가입설계상설요청](https://user-images.githubusercontent.com/84304227/124379867-75b72580-dcf4-11eb-87fb-3cc9db6c6315.PNG)

```
가입설계상태 재조회
http --json POST localhost:8080/plans/online svcId="PLA002SVC" svcFn="getPlan" ppsdsnNo="20210630180956" custNo="000000001"
```
![가입설계상설완료](https://user-images.githubusercontent.com/84304227/124379869-7c459d00-dcf4-11eb-92d8-c75129031c6c.PNG)

## DDD(Domain-Driven Design) 의 적용
도메인 모델은 특정 비지니스 맥락에서 통용되는 개념들의 관계를 잘 정의한 모형이다.
도메인 모델을 보면 각 도메인 모델과 다른 도메인 모델간의 경계가 보인다. 
여기서 사용하는 언어와 저곳에서 상용하는 언어와 개념이 상이하는 이 경계가 도메인의 경계, 컨텍스트 경계(Bounded Context)이다.
Bounded Context내의 도메인 주요개념을 표현하기 위해 도메인내에 공통으로 사용하는 언어가 유비쿼터스언어이다.
같은 컨텍스트내의 이해관계자가 사용하는 언어를 개발소스에도 사용해야 하나 아래의 이유로 영문구성을 하였다.

- 각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다: (예시는 payment 마이크로 서비스). 
- 이때 가능한 현업에서 사용하는 언어 (유비쿼터스 랭귀지)를 그대로 사용하려고 노력했다. 
- 하지만, 일부 구현에 있어서 영문이 아닌 경우는 실행이 불가능한 경우가 있기 때문에 계속 사용할 방법은 아닌것 같다. (Maven pom.xml, Kafka의 topic id, FeignClient 의 서비스 id 등은 한글로 식별자를 사용하는 경우 오류가 발생하는 것을 확인하였다)

```
package ezinsurance;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="prps_no"     , length=14) private String prpsNo    ; //청약번호
    @Column(name="act_dcd"     , length=2)  private String actDcd    ; //계좌구분코드
    @Column(name="cust_no"     , length=9)  private String custNo    ; //고객번호
    @Column(name="cust_nm"     , length=40) private String custNm    ; //고객명
    @Column(name="finin_cd"    , length=3)  private String fininCd   ; //금융기관코드
    @Column(name="finin_nm"    , length=50) private String fininNm   ; //금융기관명
    @Column(name="act_no"      , length=50) private String actNo     ; //계좌번호
    @Column(name="achd_nm"     , length=40) private String achdNm    ; //예금주명
    @Column(name="act_stcd"    , length=2)  private String actStcd   ; //계좌상태코드
    @Column(name="answ_cd"     , length=4)  private String answCd    ; //응답코드
    @Column(name="sta_vrfc_dtm", length=14) private String staVrfcDtm; //상태확인일시
    @Column(name="pay_amt"     , length=15) private BigDecimal payAmt; //결제금액
    @Column(name="pay_dtm"     , length=14) private String payDtm    ; //결제일시

    @PrePersist
    public void onPrePersist(){

    }
 
     ... // 생략
 }

```
- Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 다양한 데이터소스 유형 (RDB or NoSQL) 에 대한 별도의 처리가 없도록 
  데이터 접근 어댑터를 자동 생성하기 위하여 Spring Data REST 의 RestRepository 를 적용하였다
```
package ezinsurance.jpa;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="plans", path="plans")
public interface PlanRepository extends PagingAndSortingRepository<Plan, Long>{

    List<Plan> findByPpsdsnNo(String ppsdsnNo);

    List<Plan> findByCustNo(String custNo);

}
```
- 적용 후 REST API 의 테스트
```
# 설계처리
http POST a22d3b5447dbb4210808d20343a700a4-1771169070.ap-southeast-2.elb.amazonaws.com:8080/plans ^
ppsdsnNo="" ppsdsnDt="20210708" prdcd="P00000005" prdnm="(무)재해사망보장" custNo="000000002" custNm="양건우" ^
slctPlnrEno="1000000000" slctPlnrNm="보험사" slctDofOrgNo="999999" slctDofOrgNm="사이버창구" insPrd="20년" pmPrd="20년" ^
pmCyl="월납" sprm="80000" entAmt="50000" progSt="가입설계" 

# 설계상태 확인
http localhost:8080/plans/1

```


## 폴리글랏 퍼시스턴스

앱프런트 (app) 는 서비스 특성상 많은 사용자의 유입과 상품 정보의 다양한 콘텐츠를 저장해야 하는 특징으로 인해 
Marid DB 를 사용하기로 하였다. 
mypage 별다른 작업없이 기존의 Entity Pattern 과 Repository Pattern 적용과 데이터베이스 제품의 설정 (application.yml) 만으로 H2메모리,
alarm은 SQL DB를 사용하였다.

```
# 설계(Plan)
# application.yml

  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        dialect: org.hibernate.dialect.MySQL57Dialect
  datasource:
    url: jdbc:mysql://database-1.csvj9ae9rzmk.ap-northeast-2.rds.amazonaws.com:3306/${DATASOURCE_SCHEMA}
    username: ${DATASOURCE_USERNAME}
    password: ${DATASOURCE_PASSWORD}
    driverClassName: org.mariadb.jdbc.Driver

# (Alarm)
# application.yml
  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        dialect: org.hibernate.dialect.HSQLDialect
  datasource:
    url: jdbc:hsqldb:mem:mydatabase
    username: sa
    password: 
    driverClassName: org.hsqldb.jdbc.JDBCDriver

```

## 폴리글랏 프로그래밍

 - java 프로그래밍


## 동기식 호출 과 Fallback 처리

분석단계에서의 조건 중 하나로 청약(proposal)->결제(payment) 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 

- 상품서비스(인수기준 체크)를 호출하기 위하여 Stub과 (FeignClient) 를 이용하여 Service 대행 인터페이스 (Proxy) 를 구현 

```
#  ProductService.java

package ezinsurance.external;

@FeignClient(name="product", url="${api.url.product}")
public interface ProductService {

    @RequestMapping(method= RequestMethod.POST, path="/products/online", produces = "application/json")
    public Map<String, Object> callService(@RequestBody Map<String, String> userData);

    @RequestMapping(method= RequestMethod.POST, path="/products/chkProduct", produces = "application/json")
    public Map<String, Object> chkProduct(@RequestBody Map<String, String> userData);

}
```

- 보험료 계산시 인수조건 체크하도록 처리
```
# Proposal.java (Entity)

	ProductVO productInfo = null;
	try {

		Map<String, Object> outMap = PlanApplication.applicationContext.getBean(ProductService.class).callService(svcParam);

		System.out.println("\n##### PLA001SVC outMap : " + outMap + "\n");
		
		String jsonStr= FwkUtils.toJson(outMap);
						
		System.out.println("\n##### PLA002SVC FwkUtils.toJson(outMap) : " + jsonStr + "\n");

		productInfo = FwkUtils.jsonToObject(jsonStr, "data", ProductVO.class);

		//System.out.println("\n##### PLA002SVC productInfo : " + productInfo.toString() + "\n");

		BeanUtils.copyProperties(productInfo, out);

	}catch(Exception e) {
		throw new RuntimeException("보험료계산 오류 :: "+e.getLocalizedMessage());

		//e.printStackTrace();
	}
```

- 동기식 호출에서는 호출 시간에 따른 타임 커플링이 발생하며, 결제 시스템이 장애가 나면 주문도 못받는다는 것을 확인:


```
# 상품(product) 서비스를 잠시 내려놓음 (ctrl+c)

#보험료계산 호출

```
- 보험료계산실패
![보험료계산오류](https://user-images.githubusercontent.com/84304227/124745375-b617dd00-df5a-11eb-9284-eeaf5fec6345.PNG)

```
#상품(product) 재기동

#보험료계산 호출
```
![보험료계산정상](https://user-images.githubusercontent.com/84304227/124745560-ec555c80-df5a-11eb-8833-8af565497057.PNG)

- 또한 과도한 요청시에 서비스 장애가 도미노 처럼 벌어질 수 있다. (서킷브레이커, 폴백 처리는 운영단계에서 설명한다.)



## 비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트

결제가 이루어진 후에 숙소 시스템의 상태가 업데이트 되고, 예약 시스템의 상태가 업데이트 되며, 예약 및 취소 메시지가 전송되는 시스템과의 통신 행위는 비동기식으로 처리한다.

결제가 이루어진 후에 청약시스템으로 진행결과를 알려주는 행위는 동기식이 아니라 비동기식으로 처리되어 청약상태 및 후속 계약업무도 비동기 식으로 처리한다.

- 이를 위하여 결제이력에 기록을 남긴 후에 곧바로 결제승인이 되었다는 도메인 이벤트를 카프카로 송출한다(Publish)
 
```
package ezinsurance;

@Entity
@Table(name="Payment_table")
public class Payment {

 ...
    @PrePersist
    public void onPrePersist(){
        PayApproved payApproved = new PayApproved();
        BeanUtils.copyProperties(this, payApproved);
        payApproved.setStatus("결재승인");
        payApproved.publishAfterCommit();
        
    }

}
```
- 각각의 서비스에서는 결제승인 이벤트에 대해서 이를 수신하여 자신의 정책을 처리하도록 PolicyHandler 를 구현한다:

```
package ezinsurance;

...

@Service
public class PolicyHandler{

    @StreamListener(KafkaProcessor.INPUT)
    public void whenEventOccurred (@Payload Mypage eventInfo) {

    	System.out.println("\n\n##### eventInfo : " + eventInfo.toString() + "\n\n");
	//카프카 멘시지 수신처리.
	// 결재후에 업무처리 ...


    	String eventType = eventInfo.getEventType();


    }

}

```
실제 구현을 하자면, 카톡 등으로 상품설명서발행, 설계저장 등의 본인진행상태를 확인하고 청약업무를 진행한다.
  

메시지 서비스는 예약/결제와 완전히 분리되어있으며, 이벤트 수신에 따라 처리되기 때문에, 메시지 서비스가 유지보수로 인해 잠시 내려간 상태 라도 예약을 받는데 문제가 없다.

#메시지서비시 를 잠시 내려놓음
```
http POST a22d3b5447dbb4210808d20343a700a4-1771169070.ap-southeast-2.elb.amazonaws.com:8080/proposals \
ppsdsnNo="20210708033005" \
prdcd="P00000005" \
prdnm="(무)재해사망보장" \
custNo="000000002" \
custNm="양건우" \
slctPlnrEno="1000000000" \
slctPlnrNm="라이나" \
```

#진행상태 확인
```
http a22d3b5447dbb4210808d20343a700a4-1771169070.ap-southeast-2.elb.amazonaws.com:8080:8080/mypages     # 진행상태 안바뀜 확인
```

#마이페이지 서비스 기동


#진행상태 확인
```
http a22d3b5447dbb4210808d20343a700a4-1771169070.ap-southeast-2.elb.amazonaws.com:8080:8080/mypages     # 모든 주문의 상태 확인
```
![내보험조회](https://user-images.githubusercontent.com/84304227/124940038-965ce380-e044-11eb-82d0-5fa83ee92c84.PNG)

# 운영

# 환경구성

* EKS Cluster create
```
$ eksctl create cluster --user08-eks --version 1.15 --nodegroup-name standard-workers --node-type t3.medium --nodes 3 --nodes-min 1 --nodes-max 4
```

* EKS Cluster settings
```
$ aws eks --region ap-southeast-2 update-kubeconfig --name user08-eks
$ kubectl config current-context
$ kubectl get all
```

* ECR 인증
```
$ aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 052937454741.dkr.ecr.ap-northeast-2.amazonaws.com
```

* Metric Server 설치
```
$ kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/download/v0.3.6/components.yaml
$ kubectl get deployment metrics-server -n kube-system
```

* Kafka install (kubernetes/helm)
--Helm 3.x 설치
```
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh
```

* Helm 에게 권한을 부여하고 초기화
```
kubectl --namespace kube-system create sa tiller
kubectl create clusterrolebinding tiller --clusterrole cluster-admin --serviceaccount=kube-system:tiller
```
* 카프카 설치

 ```
helm repo add incubator https://charts.helm.sh/incubator 
helm repo update 
kubectl create ns kafka 
helm install my-kafka --namespace kafka incubator/kafka 
kubectl get all -n kafka
```

* Istio 설치
```
$ curl -L https://git.io/getLatestIstio | ISTIO_VERSION=1.4.5 sh -
$ cd istio-1.4.5
$ export PATH=$PWD/bin:$PATH
$ for i in install/kubernetes/helm/istio-init/files/crd*yaml; do kubectl apply -f $i; done
$ kubectl apply -f install/kubernetes/istio-demo.yaml
$ kubectl get pod -n istio-system
```

* Kiali 설정
```
$ kubectl edit service/kiali -n istio-system

- type 변경 : ClusterIP -> LoadBalancer
- (접속주소) http://http://ac5885beaca174095bad6d5f5779a443-1156063200.ap-northeast-2.elb.amazonaws.com:20001/kiali
```

* Namespace 생성
```
$ kubectl create namespace ezinsurance
```

* Namespace istio enabled
```
$ kubectl label namespace ezinsurance istio-injection=enabled 
```

- (설정해제 : kubectl label namespace ezdelivery istio-injection=disabled --overwrite)

* siege deploy
```
cd ezinsurance/yaml
kubectl apply -f siege.yaml 
kubectl exec -it siege -n ezinsurance -- /bin/bash
apt-get update
apt-get install httpie
```

```
apiVersion: v1
kind: Pod
metadata:
  name: siege
  namespace: ezinsurance
spec:
  containers:
    - name: siege
      image: apexacme/siege-nginx
```
# Build & Deploy

* ECR image repository
```
$ aws ecr create-repository --repository-name ezinsurance-gateway --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-product --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-customer --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-plan --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-proposal --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-payment --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-myinsurance --region ap-southeast-2
$ aws ecr create-repository --repository-name ezinsurance-alarm --region ap-southeast-2

```

* image build & push
```
$ cd gateway
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-gateway:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-gateway:latest

$ cd ../product
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-product:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-product:latest

$ cd ../customer
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-customer:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-customer:latest

$ cd ../plan
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-plan:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-plan:latest

$ cd ../proposal
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-proposal:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-proposal:latest

$ cd ../payment
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-payment:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-payment:latest

$ cd ../myinsurance
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-myinsurance:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-myinsurance:latest

$ cd ../alarm
$ mvn package
$ docker build -t 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-alarm:latest .
$ docker push 879772956301.dkr.ecr.ap-southeast-2.amazonaws.com/ezinsurance-alarm:latest

```

* Deploy
```
cd ../yaml

kubectl apply -f siege.yaml
kubectl apply -f configmap.yaml
kubectl apply -f gateway.yaml
kubectl apply -f customer.yaml
kubectl apply -f product.yaml
kubectl apply -f plan.yaml
kubectl apply -f proposal.yaml
kubectl apply -f myinsurance.yaml
kubectl apply -f proposal.yaml
kubectl apply -f payment.yaml
kubectl apply -f alarm.yaml

```
## CI/CD 설정

각 구현체들은 github의 각각의 source repository 에 구성
Image repository는 ECR 사용

## 동기식 호출 / 서킷 브레이킹 / 장애격리

개요:
여러 개의 서비스로 이루어진 시스템은 하나의 서비스의 장애 발생 시 다른 서비스가 영향을 받을 수 있음.
서비스 인스턴스간 호출로 인해 communicatinon overhead 가 발생할 경우 서비스간
회로 차단기(Circuit Breaker)를 두고 일정 시간 응답이 없는 경우 연결을 끊어서 장애가 확산 되는 것을 막을 요구가 생김
NETFLIX 가 이러한 아키텍처를 실제 그들의 서비스 네트워크에 적용해 오픈소스(a.k.a 'NETFLIX OSS')로 공개한 Hystrix가 있지만 
중앙집중적인 관리와 통제가 필요하게 되었고, 더불어 Service Discovery, Load Balancing, Fault Recovery, Metrics & Monitoring 등의 역할도 통합적으로 수행할
Istio 등장 

 
서킷 브레이킹 프레임워크의 선택: istio-injection + DestinationRule
시나리오는 청약(Proposal) --> 결제(pay) 시의 연결을 RESTful Request/Response 로 연동하여 구현이 되어있고, 결제 요청이 과도할 경우 CB 를 통하여 장애격리.

- DestinationRule 를 생성하여 circuit break 가 발생할 수 있도록 설정 최소 connection pool 설정

```
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: dr-pay
  namespace: ezinsurance
spec:
  host: payment
  trafficPolicy:
    connectionPool:
      http:
        http1MaxPendingRequests: 1
        maxRequestsPerConnection: 1
#    outlierDetection:
#      interval: 1s
#      consecutiveErrors: 2
#      baseEjectionTime: 10s
#      maxEjectionPercent: 100
```

- istio-injection 활성화 및 room pod container 확인
```
kubectl get ns -L istio-injection
kubectl label namespace ezinsurance istio-injection=enabled
````
![istio_injection](https://user-images.githubusercontent.com/84304227/124936402-7841b400-e041-11eb-8674-41ef04c71ea7.PNG)

부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:

- 동시사용자 1명, 60초 동안 부하 생성 시 모두 정상
```
siege -c1 -t60S -r10 --content-type "application/json" 'http://proposal:8080/proposals/online POST {"svcId":"NBA001SVC", "svcFn":"getCntr", "prpsNo":"20210704165943"}' -v
** SIEGE 4.0.4
** Preparing 1 concurrent users for battle.
The server is now under siege...
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online

Lifting the server siege...
Transactions:                      5 hits
Availability:                 100.00 %
Elapsed time:                   59.92 secs
Data transferred:               0.00 MB
Response time:                  1.78 secs
Transaction rate:               0.50 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    0.90
Successful transactions:           5
Failed transactions:               0
Longest transaction:            1.96
Shortest transaction:           1.66
```
서킷 브레이킹을 위한 DestinationRule 적용

```
cd ../yaml
kubectl apply -f dr-pay.yaml
```
istio-injection 적용 (기 적용완료)
```
kubectl label namespace ezinsurance istio-injection=enabled
```
* 부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:
- 동시사용자 10명
- 60초 동안 실시

```
$ siege -c10 -t60S -r10 --content-type "application/json" 'http://proposal:8080/proposals/online POST {"svcId":"NBA001SVC", "svcFn":"getCntr", "prpsNo":"20210704165943"}' -v

** SIEGE 4.0.5
** Preparing 100 concurrent users for battle.
The server is now under siege...

HTTP/1.1 200     1.32 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.32 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.32 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.16 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.33 secs:     408 bytes ==> POST http://proposal:8080/proposals/online

* 요청이 과도하여 CB를 동작함 요청을 차단

[error] socket: read error Connection reset by peer sock.c:539: Connection reset by peer
HTTP/1.1 500     0.51 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
[error] socket: read error Connection reset by peer sock.c:539: Connection reset by peer
HTTP/1.1 500     0.09 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.52 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.52 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.10 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.19 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.09 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.17 secs:     179 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 500     0.10 secs:     179 bytes ==> POST http://proposal:8080/proposals/online

* 요청을 어느정도 돌려보내고나니, 기존에 밀린 일들이 처리되었고, 회로를 닫아 요청을 다시 받기 시작

HTTP/1.1 200     1.33 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.34 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.30 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
HTTP/1.1 200     1.31 secs:     408 bytes ==> POST http://proposal:8080/proposals/online
```
* 이후 이러한 패턴이 계속 반복되면서 시스템은 도미노 현상이나 자원 소모의 폭주 없이 잘 운영됨

![istio_injection_siege](https://user-images.githubusercontent.com/84304227/124997342-641fa600-e085-11eb-8e80-c3dfbcb166cc.PNG)

```
Lifting the server siege...
Transactions:                    445 hits
Availability:                  84.28 %
Elapsed time:                  59.69 secs
Data transferred:               0.18 MB
Response time:                  1.32 secs
Transaction rate:               7.46 trans/sec
Throughput:                     0.00 MB/sec
Concurrency:                    9.83
Successful transactions:         445
Failed transactions:              83
Longest transaction:            1.74
Shortest transaction:           0.01

```
- 운영시스템은 죽지 않고 지속적으로 CB 에 의하여 적절히 회로가 열림과 닫힘이 벌어지면서 자원을 보호하고 있음을 보여줌. 
- 하지만, 84% 가 성공하였고, 26%가 실패했다는 것은 고객 사용성에 있어 좋지 않기 때문에 Retry 설정과 동적 Scale out (replica의 자동적 추가,HPA) 을 통하여 시스템을 확장 해주는 후속처리가 필요.

# kiali 화면에 서킷 브레이크 확인

kubectl get all -n istio-system

![kiali](https://user-images.githubusercontent.com/84304227/124938818-94465500-e043-11eb-966b-74e9fad21e68.PNG)

다시 부하 발생하여 DestinationRule 적용 제거하여 정상 처리 확인
```
cd ezinsurance/yaml
kubectl delete -f dr-pay.yaml
```

### 오토스케일 아웃
앞서 CB 는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다. 

- (istio injection 적용한 경우) istio injection 적용 해제
```
kubectl label namespace ezinsurance istio-injection=disabled --overwrite

kubectl apply -f customer.yaml

```

- 고객서비스 배포시 resource 설정 적용되어 있음
```
    spec:
      containers:
          ...
          resources:
            limits:
              cpu: 500m
            requests:
              cpu: 200m
```

- 고객서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려준다:
```
kubectl autoscale deploy customer -n ezinsurance --min=1 --max=10 --cpu-percent=15

```
- 워크로드를 2분 동안 걸어준다.
```
siege -c61 -t120S -r1 -v  "application/json" 'http://customer:8080/customers'
```
- 오토스케일이 어떻게 되고 있는지 모니터링을 걸어둔다
- 어느정도 시간이 흐른 후 (약 30초) 스케일 아웃이 벌어지는 것을 확인할 수 있다:
```
root@labs--620633116:/home/project/personal/ezinsurance/yaml# kubectl get deploy customer -n ezinsurance -w 
NAME       READY   UP-TO-DATE   AVAILABLE   AGE
customer   1/1     1            1           4m39s
customer   1/4     1            1           5m52s
customer   1/4     1            1           5m52s
customer   1/4     1            1           5m52s
customer   1/4     4            1           5m52s
customer   2/4     4            2           5m54s
customer   3/4     4            3           5m57s
customer   4/4     4            4           5m57s
customer   4/8     4            4           6m7s
:

root@labs--620633116:/home/project/personal/ezinsurance/yaml# kubectl get hpa -n ezinsurance
NAME       REFERENCE             TARGETS         MINPODS   MAXPODS   REPLICAS   AGE
customer   Deployment/customer   5%/15%          1         10        10         12m
plan       Deployment/plan       <unknown>/15%   1         10        1          28m
```

![auto-scale_after_pods](https://user-images.githubusercontent.com/84304227/124877556-29c2f400-e006-11eb-9e84-9984b3a016ee.PNG)


## 무정지 재배포

* 먼저 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscaler 이나 CB 설정을 제거함(위의 시나리오에서 제거되었음)

- seige 로 배포작업 직전에 워크로드를 모니터링 함.
```
siege -c61 -t120S -r1 -v  "application/json" 'http://customer:8080/customers'

** SIEGE 4.0.5
** Preparing 100 concurrent users for battle.
The server is now under siege...

HTTP/1.1 200     2.53 secs:    1849 bytes ==> GET  /customers
HTTP/1.1 200     5.12 secs:    1849 bytes ==> GET  /customers
HTTP/1.1 200     5.06 secs:    1849 bytes ==> GET  /customers
HTTP/1.1 200     5.12 secs:    1849 bytes ==> GET  /customers
HTTP/1.1 200     5.13 secs:    1849 bytes ==> GET  /customers
:

```

* 컨테이너 이미지 Update (readness, liveness 미설정 상태)
```
- kubectl apply -f customer_none.yaml
```

- seige 의 화면으로 넘어가서 Availability 가 100% 미만으로 떨어졌는지 확인
```
Transactions:                    882 hits
Availability:                  46.01 %
Elapsed time:                  27.52 secs
Data transferred:               1.56 MB
Response time:                  1.80 secs
Transaction rate:              32.05 trans/sec
Throughput:                     0.06 MB/sec
Concurrency:                   57.57
Successful transactions:         882
Failed transactions:            1035
Longest transaction:            9.16
Shortest transaction:           0.57


```
배포기간중 Availability 가 평소 100% 미만으로 가용되는 것을 확인. 원인은 쿠버네티스가 성급하게 새로 올려진 서비스를 READY 상태로 인식하여 서비스 유입을 진행한 것이기 때문. 
이를 막기위해 Readiness Probe 를 설정함:

```
# deployment.yaml 의 readiness probe 의 설정:

          readinessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10
	    
	    
- kubectl apply -f customer.yaml 실행
```

- 동일한 시나리오로 재배포 한 후 Availability 확인:

![readiness](https://user-images.githubusercontent.com/84304227/124881050-e10d3a00-e009-11eb-82f4-048c81b01861.PNG)

배포기간 동안 Availability 가 변화없기 때문에 무정지 재배포가 성공한 것으로 확인됨.


# Self-healing (Liveness Probe)
LivenessProbe : 서비스 장애를 판단하여 containerd 의 restart 여부를 판단
컨테이너가 기동 된후 initialDelaySecond에 설정된 값 만큼 대기를 했다가 periodSecond 에 정해진 주기 단위로 컨테이너의 헬스 체크를 한다. initialDelaySecond를 주는 이유는, 컨테이너가 기동 되면서 애플리케이션이 기동될텐데, 설정 정보나 각종 초기화 작업이 필요하기 때문에, 컨테이너가 기동되자 마자 헬스 체크를 하게 되면, 서비스할 준비가 되지 않았기 때문에 헬스 체크에 실패할 수 있기 때문에, 준비 기간을 주는 것이다. 준비 시간이 끝나면, periodSecond에 정의된 주기에 따라 헬스 체크를 진행하게 되면 세가지 방식을 제공
•	Command probe
•	HTTP probe
•	TCP probe

Command probe로 진행

```
          resources:
            limits:
              cpu: 500m
            requests:
              cpu: 200m
          args:
            - /bin/sh
            - -c
            - touch /tmp/healthy; sleep 90; rm -rf /tmp/healthy; sleep 600
          readinessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 100
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 1
          livenessProbe:
#            httpGet:
#              path: '/actuator/health'
#             port: 8080
            exec: 
              command:
              - cat
              - /tmp/healthy
            initialDelaySeconds: 90
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 3
# 프로브는 포드의 모든 컨테이너가 생성되고 90초후에 호출되고, 2초내에 응답해야 하며, 쿠버네티스는 프로브를 5초마다 호출한다.
# 호출의 결과가 세번이상 실패하면 컨테이는 중지되고 재시작될것이다.

kubectl get po -n ezinsurance -w

NAME                           READY   STATUS    RESTARTS   AGE
payment-7bf56d654c-f7hpr       0/1     CrashLoopBackOff   5          7m17s
payment-7bf56d654c-f7hpr       0/1     Running            6          8m38s
payment-7bf56d654c-f7hpr       0/1     CrashLoopBackOff   6          9m52s
payment-7bf56d654c-f7hpr       0/1     Running            7          12m
```

- 포드의 상태를 확인했으나 기대한 대로 되지 않음 CrashLoopBackOff (shutdown->restart) 계속 발생함.(?)

- 어플리케이션의 메모리 과부화를 발생시켜  livenessProbe 설정에 의하여 자동으로 서비스가 재시작으로 확인함.

--메모리 과부하 호출
```
public class PaymentController {

    @GetMapping("callMemleak")
    public void callMemleak() {

        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);

            System.out.println("\n=======callMemleak===========\n");

            try {
                for(;;) {
                    unsafe.allocateMemory(1024*1024);
                }
            } catch (Error e) {
                System.out.println("\n=======killing===========\n");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

siege -c1 -t1S  --content-type "application/json" 'http://payment:8080/callMemleak' -v

kubectl get po -n ezinsurance -w
proposal-59576596c5-7j7b6      0/1     Running   2          6h34m
payment-86cfb94d4b-tplgn       1/1     Running   0          5m38s
payment-86cfb94d4b-tplgn       0/1     Error     0          5m44s
payment-86cfb94d4b-tplgn       0/1     Running   1          5m45s
payment-86cfb94d4b-tplgn       1/1     Running   1          7m11s


#  ConfigMap 사용
--시스템별로 또는 운영중에 동적으로 변경 가능성이 있는 설정들을 ConfigMap을 사용하여 관리합니다.

```
#application.yml

  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        dialect: org.hibernate.dialect.MySQL57Dialect
  datasource:
    url: jdbc:mysql://database-1.csvj9ae9rzmk.ap-northeast-2.rds.amazonaws.com:3306/${DATASOURCE_SCHEMA}
    username: ${DATASOURCE_USERNAME}
    password: ${DATASOURCE_PASSWORD}
    driverClassName: org.mariadb.jdbc.Driver
    

#configmap.yaml

apiVersion: v1
kind: ConfigMap
metadata:
  name: ezinsurance-config
  namespace: ezinsurance
data:
  # maribdb 접속정보
  datasource.schema: "classicmodels"
  datasource.username: "sk????"
  datasource.password: "????????"
  api.url.payment: http://payment:8080
  api.url.proudct: http://paymemt:8080
  alarm.prefix: Hello

#yaml/plan.yaml (configmap 사용)

      containers:
        - name: order
          image: 740569282574.dkr.ecr.eu-central-1.amazonaws.com/user08-ezinsurance-order:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
#환경설정 START
          env:
            - name: DATASOURCE_SCHEMA
              valueFrom:
                configMapKeyRef:
                  name: ezinsurance-config
                  key: datasource.schema
            - name: DATASOURCE_USERNAME
              valueFrom:
                configMapKeyRef:
                  name: ezdelivery-config
                  key: datasource.username
            - name: DATASOURCE_PASSWORD
              valueFrom:
                configMapKeyRef:
                  name: ezdelivery-config
                  key: datasource.password
#환경설정 END
```		  



