import AxiosConFig from "../axiosConfig";

const getAllMedicine = async (page = 0, size = 10) => {
  let response;
  try {
    response = await AxiosConFig.get(`medicines?page=${page}&size=${size}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const createMedicine = async (body) => {
  let response;
  try {
    response = await AxiosConFig.post("medicines", body);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const deleteMedicineById = async (id) => {
  let response;
  try {
    response = await AxiosConFig.delete(`medicines/${id}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const searchMedicine = async (params) => {
  let response;
  try {
    console.log("CALL");

    response = await AxiosConFig.get(`medicines-f`, { params });
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const getMedicineById = async (id) => {
  let response;
  try {
    response = await AxiosConFig.get(`medicines/${id}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const updateMedicineById = async (body) => {
  let response;
  try {
    response = await AxiosConFig.put(`medicines`, body);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

export {
  getAllMedicine,
  createMedicine,
  deleteMedicineById,
  searchMedicine,
  getMedicineById,
  updateMedicineById,
};
