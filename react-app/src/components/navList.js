import DashboardOutlined from '@mui/icons-material/DashboardOutlined';
import Person from '@mui/icons-material/Person';
import Forum from '@mui/icons-material/Forum';
import Analytics from '@mui/icons-material/Analytics';
import FolderOpen from '@mui/icons-material/FolderOpen';
import BorderColor from '@mui/icons-material/BorderColor';
import Search from '@mui/icons-material/Search';
import { AddCircle, Cottage, Handshake, Phishing, Sailing } from '@mui/icons-material';
import { getCurrentUser } from '../service/AuthService';

const navbarDefaultList = [
  {
    icon: Search,
    desc: 'Search',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/'
  },
  {
    icon: Cottage,
    desc: 'Cottages',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/showCottages'
  },
  {
    icon: Sailing,
    desc: 'Boats',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/showBoats'
  },
  {
    icon: Phishing,
    desc: 'Adventures',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/showAdventures'
  },
  
];

export default function getNavbarList(){
  let curUser = getCurrentUser();
  console.log("TREEEENUTNI iz navbarlisteee");
  console.log(curUser);
  console.log("TREEEENUTNI iz navbarlisteee");

  //return navbarDefaultList;
  if(curUser === null || curUser === undefined || Object.keys(curUser).length === 0)
    return navbarDefaultList;

  switch(curUser.userType.name){
      case 'ROLE_ADMIN':
        return navbarDefaultList;
      case 'ROLE_CLIENT':
        return navbarClientList;
      case 'ROLE_COTTAGE_OWNER':
        return navbarCotOwnList;
      case 'ROLE_SHIP_OWNER':
        return navbarShipOwnList;
      case 'ROLE_INSTRUCTOR':
        return navbarInstList
      default:
        return navbarDefaultList;
  }
}

const navbarClientList = [
  ...navbarDefaultList,
  {
    icon: Handshake,
    desc: 'My Reservations',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/'
  }
]

const navbarCotOwnList = [
  ...navbarDefaultList,
  {
    icon: AddCircle,
    desc: 'Add new Cottage',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/addCottage'
  },
  {
    icon: FolderOpen,
    desc: 'My Cottages',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/cottages'
  },
  {
    icon: Handshake,
    desc: 'Reservations',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/showReservationsOwner'
  }
]

const navbarShipOwnList = [
  ...navbarDefaultList,
  {
    icon: AddCircle,
    desc: 'Add new Ship',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/'
  },
  {
    icon: FolderOpen,
    desc: 'My Ships',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/ships'
  },
  {
    icon: Handshake,
    desc: 'Reservations',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/'
  }
]

const navbarInstList = [
  ...navbarDefaultList,
  {
    icon: AddCircle,
    desc: 'Add new Adventure',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/addAdventure'
  },
  {
    icon: FolderOpen,
    desc: 'My Adventures',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/adventures'
  },
  {
    icon: Handshake,
    desc: 'Reservations',
    secondDesc: '',
    badge: 0,
    subList: [],
    url:'/'
  }
]

